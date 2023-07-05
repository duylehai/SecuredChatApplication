package com.FinalProject.SecuredChatApplication.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_user_id", referencedColumnName = "id")
    private User firstUser;

    @ManyToOne
    @JoinColumn(name = "second_user_id", referencedColumnName = "id")
    private User secondUser;

    private String createdAt;
    
    public Conversation() {
        this.createdAt = new java.util.Date().toString();
    }

    public Conversation(User firstUser, User secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.createdAt = new java.util.Date().toString();
    }

    public Long getId() {
        return this.id;
    }
}