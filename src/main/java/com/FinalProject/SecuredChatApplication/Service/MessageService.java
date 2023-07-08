package com.FinalProject.SecuredChatApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinalProject.SecuredChatApplication.Model.Conversation;
import com.FinalProject.SecuredChatApplication.Model.Message;
import com.FinalProject.SecuredChatApplication.Model.User;
import com.FinalProject.SecuredChatApplication.Repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final UserService userService;

    @Autowired
    public MessageService(MessageRepository messageRepository, ConversationService conversationService, UserService userService) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
        this.userService = userService;
    }

    public void addMessage(Long senderId, Long receiverId, String message, String encryptedSecretKey) {
        if (!conversationService.conversationExist(senderId, receiverId)) {
            System.out.println("Conversation doesn't exist");
            conversationService.addConversation(senderId, receiverId);
        } 

        Conversation currentConversation = conversationService.getConversation(senderId, receiverId);
        User sender = userService.getUserById(senderId);

        Message newMessage = new Message(currentConversation, sender, message, encryptedSecretKey);

        messageRepository.save(newMessage);
    }
}