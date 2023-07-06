package com.FinalProject.SecuredChatApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinalProject.SecuredChatApplication.Model.Conversation;
import com.FinalProject.SecuredChatApplication.Repository.ConversationRepository;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    
    @Autowired
    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public void addConversation(Conversation conversation) {
        conversationRepository.save(conversation);
    }
}