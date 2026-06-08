package org.example.miniproj.service;

import org.example.miniproj.entity.TokenBlacklist;
import org.example.miniproj.repository.TokenBlacklistRepository;
import org.example.miniproj.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class TokenBlacklistService {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);
    
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    public void blacklistToken(String token, String reason) {
        try {
            String tokenType = jwtTokenProvider.getTokenType(token);
            LocalDateTime expiryDate = jwtTokenProvider.getExpirationDateFromToken(token)
                    .toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();
            
            TokenBlacklist.TokenType type = "ACCESS".equals(tokenType) 
                ? TokenBlacklist.TokenType.ACCESS_TOKEN 
                : TokenBlacklist.TokenType.REFRESH_TOKEN;
            
            TokenBlacklist blacklistedToken = new TokenBlacklist(token, type, expiryDate, reason);
            tokenBlacklistRepository.save(blacklistedToken);
            
            logger.info("Token blacklisted successfully. Type: {}, Reason: {}", type, reason);
        } catch (Exception e) {
            logger.error("Error blacklisting token", e);
            throw new RuntimeException("Failed to blacklist token", e);
        }
    }
    
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }
    
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanupExpiredTokens() {
        try {
            LocalDateTime now = LocalDateTime.now();
            tokenBlacklistRepository.deleteExpiredTokens(now);
            logger.info("Cleaned up expired blacklisted tokens");
        } catch (Exception e) {
            logger.error("Error cleaning up expired tokens", e);
        }
    }
}