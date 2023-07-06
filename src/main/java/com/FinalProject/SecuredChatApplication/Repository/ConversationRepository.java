package com.FinalProject.SecuredChatApplication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FinalProject.SecuredChatApplication.Model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}