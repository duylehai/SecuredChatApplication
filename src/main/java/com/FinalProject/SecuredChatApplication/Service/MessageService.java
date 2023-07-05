package com.FinalProject.SecuredChatApplication.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinalProject.SecuredChatApplication.Model.Message;
import com.FinalProject.SecuredChatApplication.Repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationService conversationService;

    @Autowired
    public MessageService(MessageRepository messageRepository, ConversationService conversationService) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
    }

    // public void addMessage(Long sender, Long receiver, String message) {
    //     Long conversationId = conversationService.getConversationId(sender, receiver);
    //     if (conversationId == null) {
    //         conversationId = conversationService.addConversation(sender, receiver);
    //     }
    // }

    // ArrayList<Message> getMessages(Long sender, Long receiver, Integer limit, Integer offset) {
    //     Long conversationId = conversationService.getConversationId(sender, receiver);
    //     if (conversationId == null) {
    //         return new ArrayList<Message>();
    //     }
    //     ArrayList<Message> messages = messageRepository.findByConversationId(conversationId, limit, offset);
    //     return messages;
    // }
}