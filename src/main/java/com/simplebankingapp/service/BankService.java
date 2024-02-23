package com.simplebankingapp.service;

import com.simplebankingapp.entity.BankAccount;
import com.simplebankingapp.entity.User;
import com.simplebankingapp.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    UserService userService;

    public BankAccount openAccount(Long userId){
        BankAccount newBankAccount = new BankAccount();
        User user = userService.findById(userId);
        if(user==null)
            return null;
        BankAccount existingBankAccount = bankAccountRepository.findByUserId(userId);
        if(existingBankAccount!=null){
            newBankAccount.setBalance(-1);
            return newBankAccount;
        }
        newBankAccount.setUser(user);
        bankAccountRepository.save(newBankAccount);
        return newBankAccount;
    }
}
