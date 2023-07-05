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
        userRepository.save(user);
    }

    public String getUserEPW(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "";
        }
        return user.getEPW();
    }

    public void updateUserEPW(String username, String newEPW) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return;
        }
        user.setEPW(newEPW);
        userRepository.save(user);
    }
}