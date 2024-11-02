package com.example.taskmanagementback.modals;

public class UserLoginResult {
    private String token;
    private String refreshToken;
    private Long clientId;
    private String msg;

    public UserLoginResult() {
    }

    public UserLoginResult(String token, String refreshToken, Long clientId, String msg) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return "UserLoginResult{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", clientId=" + clientId +
                ", msg='" + msg + '\'' +
                '}';
    }
}
