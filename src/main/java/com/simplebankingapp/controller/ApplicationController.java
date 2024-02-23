package com.simplebankingapp.controller;

import com.simplebankingapp.dto.CreateUserRequest;
import com.simplebankingapp.dto.OpenBankAccountResponse;
import com.simplebankingapp.entity.BankAccount;
import com.simplebankingapp.entity.User;
import com.simplebankingapp.service.BankService;
import com.simplebankingapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    UserService userService;

    @Autowired
    BankService bankService;
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
    public List<User> viewAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/view-accounts")
    public List<BankAccount> viewAllAccounts(){
        return bankService.getAllAccounts();
    }

}
