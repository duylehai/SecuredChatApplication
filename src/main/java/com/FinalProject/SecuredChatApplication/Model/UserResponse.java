package com.FinalProject.SecuredChatApplication.Model;

public class UserResponse {
    private String username;
    private String privateKey;

    public UserResponse(String username, String privateKey) {
        this.username = username;
        this.privateKey = privateKey;
    }
}
