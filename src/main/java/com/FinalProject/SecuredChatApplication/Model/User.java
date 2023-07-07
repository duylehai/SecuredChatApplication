package com.FinalProject.SecuredChatApplication.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = { "username" }))
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String encryptedDummy;
    private String publicKey;
    private String encryptedPrivateKey;
    private String salt;
    private String IV;

    public User() {
    }

    public User(String username, String encryptedDummy, String publicKey, String encryptedPrivateKey, String salt, String aesIVString) {
        this.username = username;
        this.encryptedDummy = encryptedDummy;
        this.publicKey = publicKey;
        this.encryptedPrivateKey = encryptedPrivateKey;
        this.salt = salt;
        this.IV = aesIVString;
    }

    public String getDummy() {
        return this.encryptedDummy;
    }

    public String getEncryptedPrivateKey() {
        return this.encryptedPrivateKey;
    }

	public String getSalt() {
		return salt;
	}

	public String getIV() {
		return IV;
	}

    public String getUsername() {
        return this.username;
    }
	
    public String getPublicKey() {
        return this.publicKey;
    }
    
    
}