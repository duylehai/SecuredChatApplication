package com.FinalProject.SecuredChatApplication.Model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String EPW;
    private String sessionKey;
    private String createdAt;

    public User() {
        this.createdAt = new Date().toString();
    }

    public User(String username, String EPW, String sessionKey) {
        this.username = username;
        this.EPW = EPW;
        this.sessionKey = sessionKey;
        this.createdAt = new Date().toString();
    }

    public Long getId() {
        return this.id;
    }
    public String getEPW() {
        return this.EPW;
    }

    public void setEPW(String EPW) {
        this.EPW = EPW;
    }
}