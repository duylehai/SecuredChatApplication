package com.FinalProject.SecuredChatApplication.JSONAdapter;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONAdapter {
	public static Map<String, String> deserialize(String input) {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		Map<String, String> myMap = gson.fromJson(input, type);
		return myMap;
	}
	
	public static String serialize(Map<String, String> myMap) {
		//TODO: implement this
		return "";
	}
}
