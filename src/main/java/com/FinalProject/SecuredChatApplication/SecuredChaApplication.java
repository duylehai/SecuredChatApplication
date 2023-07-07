package com.FinalProject.SecuredChatApplication;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.FinalProject.SecuredChatApplication.Service.UserService;

@SpringBootApplication
@EnableJpaRepositories
public class SecuredChaApplication {
	public static void main(String[] args) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
//		SpringApplication app = new SpringApplication(SecuredChaApplication.class);
//        app.setDefaultProperties(Collections
//          .singletonMap("server.port", "8088"));
//        app.run(args);
		ApplicationContext context = SpringApplication.run(SecuredChaApplication.class, args);
		UserService userService = context.getBean(UserService.class);
		ContextProvider contextProvider = new ContextProvider();
		contextProvider.setApplicationContext(context);
		
		// ConversationService conversationService = context.getBean(ConversationService.class);
		// MessageService messageService = context.getBean(MessageService.class, conversationService);
		// Add a new user
		
//		userService.addUser("hungt1", "1234");

//		System.out.println(userService.isValidUser("hungt1", "1234"));
//
//		System.out.println(userService.isValidUser("hungt2", "1234"));
	
	}
}
