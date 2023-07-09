package com.FinalProject.SecuredChatApplication.SocketServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatController {
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/chat/{owner}/{username}")
    public String sendMsgToUser(@Payload final String message, @DestinationVariable String owner, @DestinationVariable String username) throws Exception {
    	if (! WebSocketEventListener.isActive(owner))
    		return "sender not active";
    
    	simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", message);
    	return "done";
    }
}
