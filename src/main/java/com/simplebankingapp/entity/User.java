package com.simplebankingapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;

//    @OneToOne(mappedBy = "user")
//    private BankAccount bankAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

//    public BankAccount getBankAccount() {
//        return bankAccount;
//    }
//
//    public void setBankAccount(BankAccount bankAccount) {
//        this.bankAccount = bankAccount;
//    }
}
