package com.FinalProject.SecuredChatApplication;

import java.lang.reflect.Type;
import java.util.Map;

import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.bind.annotation.*;

import com.FinalProject.SecuredChatApplication.JSONAdapter.JSONAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
public class RESTController {
	
	private final String SUCCESS_CODE = "Sir yes Sir!";
	
	@GetMapping("/greeting")
	public String greeting() {
		return  "hello user! test api";
	}
	
	@GetMapping("getPublicKey")
	public String getPublicKey() {
		return "public key"; 					// SQL this public key of authenticate server
	}
	
	@GetMapping("/login")
	public String login(@RequestBody String requestData) {
		
		String privateKey = "private key"; 		// SQL this private key of authenticate server
		
		// decrypt stuff me do this
		String decryptedData = new String(requestData);
		
		Map<String, String> myMap = JSONAdapter.deserialize(decryptedData);
		
		if (! myMap.containsKey("decryptCheck") || myMap.get("decryptCheck") != SUCCESS_CODE) {
			return "Request failed!";
		}
		
		String pw = myMap.get("password");
		String EPW = "EPW"; 					// SQL this 
		String decryptedEPW = "DEPW";			// crypto stuff me do this
		if (decryptedEPW.equals("sir yes sir!"))
		{
			String sessionKey = "sessionkey";
			String IV = "IV";
			String output = "{\"sessionKey\": \"" + sessionKey + "\"," + "\"IV\":\"" + IV + "\"}";
			String decryptedOutput = new String(output); // ctypto stuff again
			return decryptedOutput;
		}
		return "Login Failed!";
	}
	
	@GetMapping("/register")
	public String register(@RequestBody String requestData) {
		String privateKey = "private key"; 		// SQL this private key of authenticate server
		
		// decrypt stuff me do this
		String decryptedData = new String(requestData);
		Map<String, String> myMap = JSONAdapter.deserialize(decryptedData);
		
		if (! myMap.containsKey("decryptCheck") || myMap.get("decryptCheck") != SUCCESS_CODE) {
			return "Request failed!";
		}
		
		String username = myMap.get("username");
		String password = myMap.get("password");
		password = new String("sir yes sir"); // encrypt this shit, store this to DB
		
		return "register success";
	}
}
