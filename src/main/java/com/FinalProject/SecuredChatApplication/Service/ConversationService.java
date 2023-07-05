package com.FinalProject.SecuredChatApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinalProject.SecuredChatApplication.Model.Conversation;
import com.FinalProject.SecuredChatApplication.Model.User;
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

    public Long addConversation(User firstUser, User secondUser) {
        if (firstUser.getId() > secondUser.getId()) {
            User temp = firstUser;
            firstUser = secondUser;
            secondUser = temp;
        }
        Conversation conversation = new Conversation(firstUser, secondUser);
        conversationRepository.save(conversation);
        return conversation.getId();
    }

    public Long getConversationId(Long firstUserId, Long secondUserId) {
        if (firstUserId > secondUserId) {
            Long temp = firstUserId;
            firstUserId = secondUserId;
            secondUserId = temp;
        }
        Conversation conversation = conversationRepository.findByFirstUserIdAndSecondUserId(firstUserId, secondUserId);
        if (conversation == null) {
            return null;
        }
        return conversation.getId();
    }
}