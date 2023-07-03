package com.FinalProject.SecuredChaApplication.SocketServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

@Controller
public class ChatController {
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/chat/{owner}/{username}")
    public String sendMsgToUser(final String message,@PathVariable String owner, @PathVariable String username) throws Exception {
    	
    	// 
    	String sessionKeySender = "<session key>"; //query session key of owner
    	String sessionkeyOwner =  "<session key>";
    	// get Initial vector for AES implement soon
    	String decryptedMessage = new String(message); // decrypt with owner session key and initial vector
    	String encryptedMessage = new String(message); // encrypt with receiver session key and initial vector
    	
    	simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", encryptedMessage);
    	return "done";
    }
}
