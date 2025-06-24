package com.simplebankingapp.service;

import com.simplebankingapp.entity.BankAccount;
import com.simplebankingapp.entity.User;
import com.simplebankingapp.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean depositMoney(Long id, Integer amount){
        BankAccount currAccount = bankAccountRepository.findById(id).get();
        if(currAccount==null)
            return false;
        Integer balance = currAccount.getBalance();
        balance+=amount;
        currAccount.setBalance(balance);
        bankAccountRepository.save(currAccount);
        return true;
    }
    public boolean depositMoneyByUser(Long userId, Integer amount){
        BankAccount currAccount = bankAccountRepository.findByUserId(userId);
        if(currAccount==null)
            return false;
        Integer balance = currAccount.getBalance();
        balance+=amount;
        currAccount.setBalance(balance);
        bankAccountRepository.save(currAccount);
        return true;
    }
    public BankAccount getBankAccount(Long id){
        BankAccount curr = bankAccountRepository.findById(id).get();
        return curr;
    }

    public void transferMoney(BankAccount fromBankAccount, BankAccount toBankAccount, Integer amount){

        Integer sourceMoneyBeforeTransfer = fromBankAccount.getBalance();
        Integer sourceMoneyAfterTransfer = sourceMoneyBeforeTransfer-amount;
        fromBankAccount.setBalance(sourceMoneyAfterTransfer);
        Integer destinationMoneyBeforeTransfer = toBankAccount.getBalance();
        Integer destinationMoneyafterTransfer = destinationMoneyBeforeTransfer+amount;
        toBankAccount.setBalance(destinationMoneyafterTransfer);
        bankAccountRepository.save(fromBankAccount);
        bankAccountRepository.save(toBankAccount);
    }

    public List<BankAccount> getAllAccounts(){
        return bankAccountRepository.findAll();
    }
}
