package com.FinalProject.SecuredChaApplication;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
public class RESTController {
	
	@GetMapping("/greeting")
	public String greeting() {
		return  "hello user! test api";
	}
	
	@GetMapping("/login")
	public String login(@RequestBody Map<String, Object> requestData) {
		for(Map.Entry<String, Object> entry: requestData.entrySet()) {
			System.out.println(entry.getKey() + " :  " + entry.getValue());
		}
		return "";
	}
	
}
