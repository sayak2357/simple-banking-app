package com.simplebankingapp.controller;

import com.simplebankingapp.dto.CreateUserRequest;
import com.simplebankingapp.entity.User;
import com.simplebankingapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    UserService userService;
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

}
