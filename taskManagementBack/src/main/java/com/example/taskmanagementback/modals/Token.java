package com.example.taskmanagementback.modals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

    private String refreshToken;

    @JsonCreator
    public Token(@JsonProperty("token") String token) {
        this.refreshToken = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "Token{" +
                "refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
