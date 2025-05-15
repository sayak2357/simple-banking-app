package com.simplebankingapp.controller;

import com.simplebankingapp.dto.CreateUserRequest;
import com.simplebankingapp.dto.DepositMoneyRequest;
import com.simplebankingapp.dto.OpenBankAccountResponse;
import com.simplebankingapp.dto.TransferMoneyRequest;
import com.simplebankingapp.entity.BankAccount;
import com.simplebankingapp.entity.User;
import com.simplebankingapp.exceptions.UnauthorizedAccessException;
import com.simplebankingapp.service.AuthService;
import com.simplebankingapp.service.BankService;
import com.simplebankingapp.service.UserService;
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

    @PostMapping("/create-user")
    public User createUser(@RequestBody CreateUserRequest req, HttpServletRequest request) throws Exception {
//        String requestKey = request.getHeader("X-Request-Key");
//        if(requestKey.equals(null) || !requestKey.equals(apiKey))
//            throw new Exception("unauthorized");
        User newUser = new User();
        newUser.setName(req.getName());
        newUser.setAge(req.getAge());
        return userService.saveUser(newUser);
    }

    @GetMapping("/open-account")
    public OpenBankAccountResponse openBankAccount(@RequestParam(value = "id") Long userid) throws Exception {
        OpenBankAccountResponse res = new OpenBankAccountResponse();
        BankAccount newAccount = bankService.openAccount(userid);
        if(newAccount==null) {
            res.setMessage("invalid user id");
            res.setSuccess(false);
        }
        else if(newAccount.getBalance()==-1){
            res.setMessage("user with id "+userid+" already have an active account");
            res.setSuccess(false);
        }
        else{
            res.setMessage("account created successfully");
            res.setUserId(userid);
            res.setBankAccount(newAccount);
            res.setSuccess(true);
        }
        return res;
    }

    @GetMapping("/view-users")
    public List<User> viewAllUsers(@RequestHeader("x-user-id") long userId, HttpServletRequest request){
        String uri = request.getRequestURI();
        String method = request.getMethod();
        boolean isAuthorized = authService.isAuthorized(uri,method,userId);
        if(!isAuthorized)
            throw new UnauthorizedAccessException("Unauthorized access!");
        return userService.getAllUsers();
    }

    @GetMapping("/view-accounts")
    public List<BankAccount> viewAllAccounts(@RequestHeader("x-user-id") long userId, HttpServletRequest request) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        boolean isAuthorized = authService.isAuthorized(uri,method,userId);
        if(!isAuthorized)
            throw new UnauthorizedAccessException("Unauthorized access!");
        System.out.println("userId="+userId);
        return bankService.getAllAccounts();
    }

    @PostMapping("/deposit")
    public String deposit(@RequestHeader("x-user-id") long userId,@RequestBody DepositMoneyRequest req, HttpServletRequest request) throws Exception {
//        String requestKey = request.getHeader("X-Request-Key");
//        if(requestKey.equals(null) || !requestKey.equals(apiKey))
//            throw new Exception("unauthorized");

        String uri = request.getRequestURI();
        String method = request.getMethod();
        if(!authService.isAuthorized(uri,method,userId)){
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        boolean res = bankService.depositMoney(req.getBankAccountId(), req.getAmount());

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
