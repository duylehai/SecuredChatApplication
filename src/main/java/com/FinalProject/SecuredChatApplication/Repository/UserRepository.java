package com.FinalProject.SecuredChatApplication.Repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FinalProject.SecuredChatApplication.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = ?1") // JPQL
    User findByUsername(String username);
}