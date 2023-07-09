package com.FinalProject.SecuredChatApplication.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponse {
    @JsonProperty("username")
    private String username;

    @JsonProperty("private_key")
    private String privateKey;

    public UserResponse(String username, String privateKey) {
        this.username = username;
        this.privateKey = privateKey;
    }
}
