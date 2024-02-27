package com.simplebankingapp.service;

import com.simplebankingapp.entity.BankAccount;
import com.simplebankingapp.entity.User;
import com.simplebankingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        userRepository.save(user);

        return user;
    }

    public User findById(Long userId){
        return userRepository.findById(userId).get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
