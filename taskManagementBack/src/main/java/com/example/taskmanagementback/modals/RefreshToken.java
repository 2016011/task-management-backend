package com.example.taskmanagementback.modals;

public class RefreshToken {
    private String msg;
    private String authToken;
    private String refreshToken;

    public RefreshToken() {
    }

    public RefreshToken(String msg, String authToken, String refreshToken) {
        this.msg = msg;
        this.authToken = authToken;
        this.refreshToken = refreshToken;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "msg='" + msg + '\'' +
                ", authToken='" + authToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
