package com.FinalProject.SecuredChatApplication.SocketServer;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    protected static Set<String> activeUsers = new HashSet<>();
    
    public static boolean isActive(String username) {
    	return activeUsers.contains(username);
    }
    
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = event.getUser();
        String username = user.getName();
        activeUsers.remove(username);
        if(username != null) {
        	System.out.println("User Disconnected : " + username);
            messagingTemplate.convertAndSend("/topic/public", "{\"from\": \"" + username + "\", \"text\": \"disconnected\"}");
        }
    }
}
