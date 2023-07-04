package com.FinalProject.SecuredChatApplication.Cryptography;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

import java.security.spec.*;
import java.util.Base64;

import javax.crypto.*;

import javafx.util.*;

public class RSA {
	public static Pair<String, String> generatekeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair pair = generator.generateKeyPair();
		PrivateKey privateKey = pair.getPrivate();
		PublicKey publicKey = pair.getPublic();
		Pair<String,String> res = new Pair<>(new String(privateKey.getEncoded()), new String(publicKey.getEncoded()));
		return res;
	}
	
	private static PublicKey stringToPubKey(String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(keyString.getBytes("utf-8")));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey key = keyFactory.generatePublic(keySpec);
		return key;
	}
	
	private static PrivateKey stringToPrivateKey(String keyString) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString.getBytes("tuf-8")));
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PrivateKey priv = fact.generatePrivate(keySpec);
		
		return priv;
	}
	
	
	public static String encrypt(String pub, String input) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, stringToPubKey(pub));
		
		byte[] secretMessageBytes = input.getBytes(StandardCharsets.UTF_8);
		byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
		
		String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
		
		return encodedMessage;
	}
	
	public static String decrypt(String priv, String input) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		
		Cipher decryptCipher = Cipher.getInstance("RSA");
		decryptCipher.init(Cipher.DECRYPT_MODE, stringToPrivateKey(priv));
		
		byte[] decryptedMessageBytes = decryptCipher.doFinal(input.getBytes());
		String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
		
		return decryptedMessage;
	}
}
