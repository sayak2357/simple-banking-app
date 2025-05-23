package com.simplebankingapp.dto;

import lombok.Getter;
import lombok.Setter;


public class CreateUserRequest {

    private String name;
    private Integer age;
    private String password;

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
