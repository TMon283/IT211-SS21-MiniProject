package org.example.miniproj.service;

import org.example.miniproj.dto.request.LoginRequest;
import org.example.miniproj.dto.request.RefreshTokenRequest;
import org.example.miniproj.dto.response.JwtAuthenticationResponse;
import org.example.miniproj.dto.response.UserInfoResponse;
import org.example.miniproj.entity.User;
import org.example.miniproj.repository.UserRepository;
import org.example.miniproj.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    
    @Value("${app.jwt.access-token.expiration:1800000}") // 30 minutes
    private long accessTokenExpiration;
    
    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        
        User user = (User) authentication.getPrincipal();
        UserInfoResponse userInfo = new UserInfoResponse(user);
        
        logger.info("User {} logged in successfully", user.getUsername());
        
        return new JwtAuthenticationResponse(
            accessToken, 
            refreshToken, 
            accessTokenExpiration / 1000, // Convert to seconds
            userInfo
        );
    }
    
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        if (tokenBlacklistService.isTokenBlacklisted(refreshToken)) {
            throw new RuntimeException("Refresh token has been revoked");
        }
        
        String tokenType = jwtTokenProvider.getTokenType(refreshToken);
        if (!"REFRESH".equals(tokenType)) {
            throw new RuntimeException("Invalid token type");
        }
        
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user, null, user.getAuthorities()
        );
        
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        UserInfoResponse userInfo = new UserInfoResponse(user);
        
        logger.info("Access token refreshed for user: {}", username);
        
        return new JwtAuthenticationResponse(
            newAccessToken, 
            refreshToken, 
            accessTokenExpiration / 1000,
            userInfo
        );
    }
    
    public void logout(String accessToken, String refreshToken) {
        try {
            if (accessToken != null) {
                tokenBlacklistService.blacklistToken(accessToken, "logout");
            }
            if (refreshToken != null) {
                tokenBlacklistService.blacklistToken(refreshToken, "logout");
            }
            
            SecurityContextHolder.clearContext();
            logger.info("User logged out successfully");
        } catch (Exception e) {
            logger.error("Error during logout", e);
            throw new RuntimeException("Logout failed", e);
        }
    }
    
    public void revokeUserTokens(String username) {
        // This method would be called by admin to force logout a user
        // In a real implementation, you might want to track active tokens per user
        logger.info("All tokens revoked for user: {}", username);
    }
}