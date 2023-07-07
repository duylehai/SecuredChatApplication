package com.FinalProject.SecuredChatApplication.Model;

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
    private Conversation conversation;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    private String encryptedMessage;
    private String encryptedSecretKey;

    public Message() {

    }

    public Message(Conversation conversation, User sender, String encryptedMessage, String encryptedSecretKey) {
        this.conversation = conversation;
        this.sender = sender;
        this.encryptedMessage = encryptedMessage;
        this.encryptedSecretKey = encryptedSecretKey;
    }
}
