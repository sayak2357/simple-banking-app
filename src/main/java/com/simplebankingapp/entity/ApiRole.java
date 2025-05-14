package com.simplebankingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="api_role")
public class ApiRole {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uri;
    String httpMethod;

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
