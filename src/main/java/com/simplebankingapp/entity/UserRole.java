package com.simplebankingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="user_role")
public class UserRole {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String role;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getRole() {
        return role;
    }

    public void setRoles(String role) {
        this.role = role;
    }

}

