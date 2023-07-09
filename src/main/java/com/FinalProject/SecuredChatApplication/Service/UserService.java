package com.FinalProject.SecuredChatApplication.Service;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinalProject.SecuredChatApplication.Cryptography.AES;
import com.FinalProject.SecuredChatApplication.Model.User;
import com.FinalProject.SecuredChatApplication.Repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

   public void addUser(String username, String password) {
       String sha = "test";

       // public key
       String publicKey = "public key";

       // private key
       String privateKey = "encrypted private key with sha(password)";

       String dummy = "encrypted 'dummy' with sha(password)";

       User user = new User(username, dummy, publicKey, privateKey, "a", "a");
       
       try {
           userRepository.save(user);
       } catch (Exception e) {
           System.out.println("Error: " + e.getMessage());
       }
   }

    // return null if invalid
    // else return encrypted private key
    public User isValidUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        
        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(user.getIV()));
        SecretKey aesKey = AES.getKeyFromPassword(password, user.getSalt());
        String dummy = AES.decrypt(user.getDummy(), aesKey , iv);
        
        if (!dummy.equals("dummy dummy dummy")) {
            return null;
        }
        return user;
    }
    
    public String getUserPublicKey(String username) {
    	return userRepository.findByUsername(username).getPublicKey();
    }

    public User getUserById(Long senderId) {
        return userRepository.findById(senderId).get();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
}