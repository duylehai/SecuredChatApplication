package com.FinalProject.SecuredChatApplication;

import java.lang.reflect.Type;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
public class RESTController {
	
	@GetMapping("/greeting")
	public String greeting() {
		return  "hello user! test api";
	}
	
	@GetMapping("getPublicKey")
	public String getPublicKey() {
		return "public key"; 					// SQL this public key of authenticate server
	}
	
	@GetMapping("/login/{username}")
	public String login(@RequestBody String requestData) {
		
		String privateKey = "private key"; 		// SQL this private key of authenticate server
		
		// decrypt stuff me do this
		String decryptedData = new String(requestData);
		
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		Map<String, String> myMap = gson.fromJson(decryptedData, type);
		
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
	
}
