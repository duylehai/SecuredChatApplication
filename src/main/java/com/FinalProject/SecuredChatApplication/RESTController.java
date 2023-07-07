package com.FinalProject.SecuredChatApplication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.FinalProject.SecuredChatApplication.Cryptography.AES;
import com.FinalProject.SecuredChatApplication.Cryptography.RSA;
import com.FinalProject.SecuredChatApplication.JSONAdapter.JSONAdapter;
import com.FinalProject.SecuredChatApplication.Model.User;
import com.FinalProject.SecuredChatApplication.Service.UserService;
import com.FinalProject.SecuredChatApplication.SocketServer.WebSocketEventListener;

import javafx.util.Pair;

@RestController
public class RESTController {
	private final UserService userService = ContextProvider.getApplicationContext().getBean(UserService.class);
	
	@GetMapping("/greeting")
	public String greeting() {
		return  "hello user! test api";
	}
	
	@GetMapping("/getUserPublicKey/{username}")
	public String getPublicKey(@PathVariable("username") String username) {
		return userService.getUserPublicKey(username);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody String requestData) { 
		Map<String, String> myMap = JSONAdapter.deserialize(requestData);
		String pw = myMap.get("password");
		String username = myMap.get("username");
		
		try {	
			User user = userService.isValidUser(username, pw);
			
			if (user == null)
				return "Login Failed!";
			
			Map<String, String> respond = new HashMap<>();
			respond.put("private key", user.getEncryptedPrivateKey());
			respond.put("salt", user.getSalt());
			respond.put("IV", user.getIV());
			
			// NOTE: serialize method is NOT IMPLEMETED.
			return JSONAdapter.serialize(respond);
		} catch (Exception e) {
			e.printStackTrace();
			return "login failed";
		}
	}
	
	@GetMapping("/register")
	public String register(@RequestBody String requestData) {
		Map<String, String> myMap = JSONAdapter.deserialize(requestData);
		String username = myMap.get("username");
		String password = myMap.get("password");
		
		try {
			Pair<String, String> keyPair = RSA.generatekeyPair();
			String salt = randomSalt();
			
			SecretKey aesKey = AES.getKeyFromPassword(password, salt);
			IvParameterSpec aesIv = AES.generateIv();
			String encryptedPrivateKey = AES.encrypt(keyPair.getKey(), aesKey, aesIv);
			String encryptedDummy = AES.encrypt("dummy dummy dummy", aesKey, aesIv);
			String aesIVString = Base64.getEncoder().encodeToString(aesIv.getIV());
			User user = new User(username, encryptedDummy, keyPair.getValue(), encryptedPrivateKey, salt, aesIVString);
			
			userService.addUser(user);
			
			return "register success";
		} catch (Exception e) {
			e.printStackTrace();
			return "register failed";
		}
	}
	
	@GetMapping("/pullMessage/{target}")
	public String pullMessage(@RequestBody final String requestData,@PathVariable("target") String receiver) {
		Map<String, String> myMap = JSONAdapter.deserialize(requestData);
		String sender = myMap.get("username");
		// NOT IMPLEMENTED YET
		String respond = "";
		return "";
	}
	
	
	@GetMapping("/isActive/{username}")
	public String isActive(@PathVariable("username") String username, @RequestBody String requestData){
		
		String decryptedData = new String(requestData);
		Map<String, String> myMap = JSONAdapter.deserialize(decryptedData);
		String receivedSession = myMap.get("sessionKey");
		
		if (!receivedSession.equals("get session key from sql")) // SQL this
			return "request failed";
		
		if (WebSocketEventListener.isActive(username)) {
			return "true";
		}
		return "false";
	}
	
	private String randomSalt() {
		byte[] byteSalt = new byte[10];
		new Random().nextBytes(byteSalt);
		String salt = new String(byteSalt, StandardCharsets.UTF_8);
		
		return salt;
	}
	
	
}
