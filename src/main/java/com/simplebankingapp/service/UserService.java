package com.simplebankingapp.service;

import com.simplebankingapp.entity.User;
import com.simplebankingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        userRepository.save(user);

        return user;
    }
}
