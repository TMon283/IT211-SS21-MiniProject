package org.example.miniproj.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_blacklist")
public class TokenBlacklist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, length = 1000)
    private String token;
    
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    
    private LocalDateTime expiryDate;
    
    private LocalDateTime blacklistedAt = LocalDateTime.now();
    
    private String reason; // logout, admin_revoke, etc.
    
    // Constructors
    public TokenBlacklist() {}
    
    public TokenBlacklist(String token, TokenType tokenType, LocalDateTime expiryDate, String reason) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiryDate = expiryDate;
        this.reason = reason;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public TokenType getTokenType() { return tokenType; }
    public void setTokenType(TokenType tokenType) { this.tokenType = tokenType; }
    
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    
    public LocalDateTime getBlacklistedAt() { return blacklistedAt; }
    public void setBlacklistedAt(LocalDateTime blacklistedAt) { this.blacklistedAt = blacklistedAt; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public enum TokenType {
        ACCESS_TOKEN, REFRESH_TOKEN
    }
}