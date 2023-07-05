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

import com.FinalProject.SecuredChatApplication.Model.User;
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
		// ConversationService conversationService = context.getBean(ConversationService.class);
		// MessageService messageService = context.getBean(MessageService.class, conversationService);
		// Add a new user
		User user = new User("meow", "meow1", "meow2");

		userService.addUser(user);

		User user2 = new User("hungt1", "test", "test1");

		userService.addUser(user2);
		// UserService userService = context.getBean(UserService.class);
		// userService.addUser(user);

		userService.updateUserEPW("hungt1", "test2");
		System.out.println(userService.getUserEPW("hungt1"));

	
	}
}
