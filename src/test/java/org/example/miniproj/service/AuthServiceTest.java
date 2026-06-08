package org.example.miniproj.service;

import org.example.miniproj.dto.request.LoginRequest;
import org.example.miniproj.dto.request.RefreshTokenRequest;
import org.example.miniproj.dto.response.JwtAuthenticationResponse;
import org.example.miniproj.entity.User;
import org.example.miniproj.repository.UserRepository;
import org.example.miniproj.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    
    @Mock
    private TokenBlacklistService tokenBlacklistService;
    
    @InjectMocks
    private AuthService authService;
    
    @Mock
    private Authentication authentication;
    
    private User testUser;
    private LoginRequest loginRequest;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setFullName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(User.Role.PATIENT);
        
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }
    
    @Test
    void testLoginSuccess() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken(authentication)).thenReturn("refresh_token");
        
        // Act
        JwtAuthenticationResponse response = authService.login(loginRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("access_token", response.getAccessToken());
        assertEquals("refresh_token", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertNotNull(response.getUserInfo());
        assertEquals("testuser", response.getUserInfo().getUsername());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateAccessToken(authentication);
        verify(jwtTokenProvider).generateRefreshToken(authentication);
    }
    
    @Test
    void testRefreshTokenSuccess() {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest("valid_refresh_token");
        when(jwtTokenProvider.validateToken("valid_refresh_token")).thenReturn(true);
        when(tokenBlacklistService.isTokenBlacklisted("valid_refresh_token")).thenReturn(false);
        when(jwtTokenProvider.getTokenType("valid_refresh_token")).thenReturn("REFRESH");
        when(jwtTokenProvider.getUsernameFromToken("valid_refresh_token")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(jwtTokenProvider.generateAccessToken(any(Authentication.class))).thenReturn("new_access_token");
        
        // Act
        JwtAuthenticationResponse response = authService.refreshToken(request);
        
        // Assert
        assertNotNull(response);
        assertEquals("new_access_token", response.getAccessToken());
        assertEquals("valid_refresh_token", response.getRefreshToken());
        assertNotNull(response.getUserInfo());
        
        verify(jwtTokenProvider).validateToken("valid_refresh_token");
        verify(tokenBlacklistService).isTokenBlacklisted("valid_refresh_token");
        verify(jwtTokenProvider).getTokenType("valid_refresh_token");
        verify(jwtTokenProvider).getUsernameFromToken("valid_refresh_token");
        verify(userRepository).findByUsername("testuser");
        verify(jwtTokenProvider).generateAccessToken(any(Authentication.class));
    }
    
    @Test
    void testRefreshTokenWithInvalidToken() {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest("invalid_refresh_token");
        when(jwtTokenProvider.validateToken("invalid_refresh_token")).thenReturn(false);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refreshToken(request);
        });
        assertEquals("Invalid refresh token", exception.getMessage());
        
        verify(jwtTokenProvider).validateToken("invalid_refresh_token");
    }
    
    @Test
    void testRefreshTokenWithBlacklistedToken() {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest("blacklisted_refresh_token");
        when(jwtTokenProvider.validateToken("blacklisted_refresh_token")).thenReturn(true);
        when(tokenBlacklistService.isTokenBlacklisted("blacklisted_refresh_token")).thenReturn(true);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refreshToken(request);
        });
        assertEquals("Refresh token has been revoked", exception.getMessage());
        
        verify(jwtTokenProvider).validateToken("blacklisted_refresh_token");
        verify(tokenBlacklistService).isTokenBlacklisted("blacklisted_refresh_token");
    }
    
    @Test
    void testRefreshTokenWithWrongTokenType() {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest("access_token_instead");
        when(jwtTokenProvider.validateToken("access_token_instead")).thenReturn(true);
        when(tokenBlacklistService.isTokenBlacklisted("access_token_instead")).thenReturn(false);
        when(jwtTokenProvider.getTokenType("access_token_instead")).thenReturn("ACCESS");
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refreshToken(request);
        });
        assertEquals("Invalid token type", exception.getMessage());
        
        verify(jwtTokenProvider).validateToken("access_token_instead");
        verify(tokenBlacklistService).isTokenBlacklisted("access_token_instead");
        verify(jwtTokenProvider).getTokenType("access_token_instead");
    }
    
    @Test
    void testLogoutSuccess() {
        // Arrange
        String accessToken = "valid_access_token";
        String refreshToken = "valid_refresh_token";
        
        // Act
        assertDoesNotThrow(() -> {
            authService.logout(accessToken, refreshToken);
        });
        
        // Assert
        verify(tokenBlacklistService).blacklistToken(accessToken, "logout");
        verify(tokenBlacklistService).blacklistToken(refreshToken, "logout");
    }
    
    @Test
    void testLogoutWithNullTokens() {
        // Act
        assertDoesNotThrow(() -> {
            authService.logout(null, null);
        });
        
        // Assert
        verify(tokenBlacklistService, never()).blacklistToken(any(), any());
    }
}