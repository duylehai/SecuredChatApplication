package com.FinalProject.SecuredChatApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinalProject.SecuredChatApplication.Model.Conversation;
import com.FinalProject.SecuredChatApplication.Model.User;
import com.FinalProject.SecuredChatApplication.Repository.ConversationRepository;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    
    @Autowired
    public ConversationService(ConversationRepository conversationRepository, UserService userService) {
        this.conversationRepository = conversationRepository;
        this.userService = userService;
    }

    public void addConversation(Conversation conversation) {
        conversationRepository.save(conversation);
    }

    public boolean conversationExist(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            return false;
        }

        if (senderId > receiverId) {
            Long temp = senderId;
            senderId = receiverId;
            receiverId = temp;
        }

        return conversationRepository.existsByUserAIdAndUserBId(senderId, receiverId);
    }

    public void addConversation(Long senderId, Long receiverId) {
        if (senderId > receiverId) {
            Long temp = senderId;
            senderId = receiverId;
            receiverId = temp;
        }

        if (conversationRepository.existsByUserAIdAndUserBId(senderId, receiverId)) {
            return;
        }

        User userA = userService.getUserById(senderId);
        System.out.println("User A: " + userA.getUsername());
        
        Conversation conversation = new Conversation(
            userService.getUserById(senderId), 
            userService.getUserById(receiverId)
        );

        System.out.println("Conversation added");

        conversationRepository.save(conversation);
    }

    public Conversation getConversation(Long senderId, Long receiverId) {
        if (senderId > receiverId) {
            Long temp = senderId;
            senderId = receiverId;
            receiverId = temp;
        }

        return conversationRepository.findByUserAIdAndUserBId(senderId, receiverId);
    }
}