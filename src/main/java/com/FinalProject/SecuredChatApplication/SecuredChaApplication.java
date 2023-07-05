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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.FinalProject.SecuredChatApplication.Model.User;
import com.FinalProject.SecuredChatApplication.Repository.UserRepository;
import com.FinalProject.SecuredChatApplication.Service.UserService;

@SpringBootApplication
public class SecuredChaApplication {
	public static void main(String[] args) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
//		SpringApplication app = new SpringApplication(SecuredChaApplication.class);
//        app.setDefaultProperties(Collections
//          .singletonMap("server.port", "8088"));
//        app.run(args);
		ApplicationContext context = SpringApplication.run(SecuredChaApplication.class, args);
		// UserService userService = context.getBean(UserService.class);

		// Add a new user
		User user = new User();
		user.setId(40L);
		user.setName("John");

		// userService.addUser(user);

		UserService userService = context.getBean(UserService.class);
		userService.addUser(user);

		System.out.println(user.toString());

	
	}
}
