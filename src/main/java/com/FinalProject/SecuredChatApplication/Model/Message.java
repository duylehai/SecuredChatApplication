package com.FinalProject.SecuredChatApplication.Model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Conversation.class)
    @JoinColumn(name = "conversation_id", referencedColumnName = "id")
    private Long conversationId;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Long senderId;

    private String message;
    
    private String createdAt;

    public Message() {
        this.createdAt = new Date().toString();
    }

    public Message(Long conversationId, Long senderId, String message) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.message = message;
        this.createdAt = new Date().toString();
    }
}
