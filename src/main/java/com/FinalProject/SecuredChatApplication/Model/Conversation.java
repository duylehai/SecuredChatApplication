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

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_a_id", referencedColumnName = "id")
    private User userA;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_b_id", referencedColumnName = "id")
    private User userB;

    public Conversation() {
    }

    public Conversation(User userA, User userB) {
        this.userA = userA;
        this.userB = userB;
    }
}