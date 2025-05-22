package com.simplebankingapp.dto;

public class UserToken {
    private String token;
    private boolean isError;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public UserToken(String token, boolean isError) {
        this.token = token;
        this.isError = isError;
    }
}
