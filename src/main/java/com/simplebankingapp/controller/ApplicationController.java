package com.simplebankingapp.controller;

import com.simplebankingapp.constants.Constants;
import com.simplebankingapp.dto.*;
import com.simplebankingapp.entity.BankAccount;
import com.simplebankingapp.entity.User;
import com.simplebankingapp.exceptions.UnauthorizedException;
import com.simplebankingapp.service.AuthService;
import com.simplebankingapp.service.BankService;
import com.simplebankingapp.service.UserService;
import com.simplebankingapp.util.JwtUtil;
import com.simplebankingapp.util.MD5Util;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/banking-system")
@RestController
public class ApplicationController {

    @Autowired
    UserService userService;

    @Autowired
    BankService bankService;

    @Autowired
    AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public UserToken authenticate(@RequestBody LoginRequest user) {
        String username = user.getUsername();
        String password = MD5Util.md5(user.getPassword());
        boolean auth = userService.auth(username,password);
        if (auth) {
            return new UserToken(jwtUtil.generateToken(username),false);
        }
        return new UserToken("Invalid credentials",true);
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody CreateUserRequest req, HttpServletRequest request) throws Exception {
//        String requestKey = request.getHeader("X-Request-Key");
//        if(requestKey.equals(null) || !requestKey.equals(apiKey))
//            throw new Exception("unauthorized");
        User newUser = new User();
        newUser.setUsername(req.getName());
        newUser.setAge(req.getAge());
        newUser.setPassword(MD5Util.md5(req.getPassword()));
        User createdUser = userService.saveUser(newUser);
        userService.createUserRole(createdUser.getId(), Constants.REGULAR_USER);
        return createdUser;
    }

    @GetMapping("/open-account")
    public OpenBankAccountResponse openBankAccount(@RequestHeader("Authorization") String authHeader) throws Exception {
        OpenBankAccountResponse res = new OpenBankAccountResponse();
        String token = authHeader.substring(7);
        String username = jwtUtil.validateToken(token);
        long userId = userService.getIdFromUsername(username);
        BankAccount newAccount = bankService.openAccount(userId);
        if(newAccount==null) {
            res.setMessage("invalid user id");
            res.setSuccess(false);
        }
        else if(newAccount.getBalance()==-1){
            res.setMessage("user with id "+userId+" already have an active account");
            res.setSuccess(false);
        }
        else{
            res.setMessage("account created successfully");
            res.setUserId(userId);
            res.setBankAccount(newAccount);
            res.setSuccess(true);
        }
        return res;
    }

    @GetMapping("/view-users")
    public List<User> viewAllUsers(@RequestHeader("Authorization") String authHeader, HttpServletRequest request){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String uri = request.getRequestURI();
            String method = request.getMethod();
            String token = authHeader.substring(7);
            String username = jwtUtil.validateToken(token);
            long userId = userService.getIdFromUsername(username);
            if(userId==-1)
                throw new UnauthorizedException("Invalid user!");
            boolean isAuthorized = authService.isAuthorized(uri, method, userId);
            if (!isAuthorized)
                throw new UnauthorizedException("Unauthorized access!");
            return userService.getAllUsers();
        }
        throw new UnauthorizedException("Bearer token not present in request!");
    }

    @GetMapping("/view-accounts")
    public List<BankAccount> viewAllAccounts(@RequestHeader("Authorization") String authHeader, HttpServletRequest request) throws Exception {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String uri = request.getRequestURI();
            String method = request.getMethod();
            String token = authHeader.substring(7);
            String username = jwtUtil.validateToken(token);
            long userId = userService.getIdFromUsername(username);
            boolean isAuthorized = authService.isAuthorized(uri, method, userId);
            if (!isAuthorized)
                throw new UnauthorizedException("Unauthorized access!");
            //System.out.println("userId=" + userId);
            return bankService.getAllAccounts();
        }
        throw new UnauthorizedException("Bearer token not present in request!");
    }

    @PostMapping("/deposit")
    public String deposit(@RequestHeader("Authorization") String authHeader, @RequestBody DepositMoneyRequest req, HttpServletRequest request) throws Exception {
//        String requestKey = request.getHeader("X-Request-Key");
//        if(requestKey.equals(null) || !requestKey.equals(apiKey))
//            throw new Exception("unauthorized");
        String token = authHeader.substring(7);
        String username = jwtUtil.validateToken(token);
        long userId = userService.getIdFromUsername(username);
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if(!authService.isAuthorized(uri,method,userId)){
            throw new UnauthorizedException("Unauthorized access");
        }
        boolean res = bankService.depositMoneyByUser(userId, req.getAmount());

        return res ? "amount "+req.getAmount()+" deposited successfully": "invalid bank acount number";
    }

    @PostMapping("/transfer-money")
    public String transfer(@RequestBody TransferMoneyRequest req, HttpServletRequest request) throws Exception {
//        String requestKey = request.getHeader("X-Request-Key");
//        if(requestKey.equals(null) || !requestKey.equals(apiKey))
//            throw new Exception("unauthorized");
        Integer amount = req.getAmount();
        Long fromBankAccountId = req.getFromBankAccountId();
        Long toBankAccountId = req.getToBankAccountId();
        if(fromBankAccountId==toBankAccountId)
            throw new Exception("source and destination accounts can't be same");
        BankAccount fromBankAccount = bankService.getBankAccount(fromBankAccountId);
        BankAccount toBankAccount = bankService.getBankAccount(toBankAccountId);
        if(fromBankAccount==null)
            throw new Exception("invalid 'from' bank account id");
        if(toBankAccount==null)
            throw new Exception("invalid 'to' bank account id");
        if(fromBankAccount.getBalance()<amount)
            throw new Exception("insufficient funds in source account");
        bankService.transferMoney(fromBankAccount, toBankAccount,amount);

        return "money transferred successfully";
    }

}
