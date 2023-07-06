package com.FinalProject.SecuredChatApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        User user = new User(username, dummy, publicKey, privateKey);
        
        try {
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // return null if invalid
    // else return encrypted private key
    public User isValidUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        String sha = "test";
        if (!user.getDummy().equals("encrypted 'dummy' with sha(password)")) {
            return null;
        }
        return user;
    }
}