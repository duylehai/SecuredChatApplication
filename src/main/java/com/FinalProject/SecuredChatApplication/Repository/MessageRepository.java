package com.FinalProject.SecuredChatApplication.Repository;

import java.util.ArrayList;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FinalProject.SecuredChatApplication.Model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}