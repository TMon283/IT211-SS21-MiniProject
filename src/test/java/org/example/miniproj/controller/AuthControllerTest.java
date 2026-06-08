package org.example.miniproj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.miniproj.dto.request.LoginRequest;
import org.example.miniproj.dto.request.RefreshTokenRequest;
import org.example.miniproj.dto.response.JwtAuthenticationResponse;
import org.example.miniproj.dto.response.UserInfoResponse;
import org.example.miniproj.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AuthService authService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private LoginRequest loginRequest;
    private RefreshTokenRequest refreshTokenRequest;
    private JwtAuthenticationResponse authResponse;
    
    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
        
        refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("refresh_token");
        
        UserInfoResponse userInfo = new UserInfoResponse();
        userInfo.setUsername("testuser");
        userInfo.setFullName("Test User");
        userInfo.setEmail("test@example.com");
        userInfo.setRole("PATIENT");
        
        authResponse = new JwtAuthenticationResponse();
        authResponse.setAccessToken("access_token");
        authResponse.setRefreshToken("refresh_token");
        authResponse.setExpiresIn(1800L);
        authResponse.setUserInfo(userInfo);
    }
    
    @Test
    void testLoginSuccess() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.accessToken").value("access_token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh_token"))
                .andExpect(jsonPath("$.data.userInfo.username").value("testuser"));
        
        verify(authService).login(any(LoginRequest.class));
    }
    
    @Test
    void testLoginFailure() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Invalid credentials"));
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
        
        verify(authService).login(any(LoginRequest.class));
    }
    
    @Test
    void testLoginWithInvalidInput() throws Exception {
        // Arrange
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setUsername(""); // Empty username
        invalidRequest.setPassword(""); // Empty password
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"));
        
        verify(authService, never()).login(any());
    }
    
    @Test
    void testRefreshTokenSuccess() throws Exception {
        // Arrange
        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(authResponse);
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Token refreshed successfully"))
                .andExpect(jsonPath("$.data.accessToken").value("access_token"));
        
        verify(authService).refreshToken(any(RefreshTokenRequest.class));
    }
    
    @Test
    void testRefreshTokenFailure() throws Exception {
        // Arrange
        when(authService.refreshToken(any(RefreshTokenRequest.class)))
                .thenThrow(new RuntimeException("Invalid refresh token"));
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid or expired refresh token"));
        
        verify(authService).refreshToken(any(RefreshTokenRequest.class));
    }
    
    @Test
    @WithMockUser
    void testLogoutSuccess() throws Exception {
        // Arrange
        doNothing().when(authService).logout(any(), any());
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/logout")
                .with(csrf())
                .header("Authorization", "Bearer access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logout successful"));
        
        verify(authService).logout(eq("access_token"), eq("refresh_token"));
    }
    
    @Test
    @WithMockUser
    void testLogoutWithoutRefreshToken() throws Exception {
        // Arrange
        doNothing().when(authService).logout(any(), any());
        
        // Act & Assert
        mockMvc.perform(post("/api/auth/logout")
                .with(csrf())
                .header("Authorization", "Bearer access_token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logout successful"));
        
        verify(authService).logout(eq("access_token"), isNull());
    }
}