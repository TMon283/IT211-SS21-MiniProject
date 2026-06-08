package org.example.miniproj.service;

import org.example.miniproj.dto.request.CreateUserRequest;
import org.example.miniproj.dto.response.UserInfoResponse;
import org.example.miniproj.entity.User;
import org.example.miniproj.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private AuthService authService;
    
    @InjectMocks
    private UserService userService;
    
    private CreateUserRequest createUserRequest;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setPassword("password123");
        createUserRequest.setFullName("Test User");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPhoneNumber("0123456789");
        createUserRequest.setRole("PATIENT");
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setFullName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(User.Role.PATIENT);
        testUser.setEnabled(true);
    }
    
    @Test
    void testCreateUserSuccess() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act
        UserInfoResponse response = userService.createUser(createUserRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("Test User", response.getFullName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("PATIENT", response.getRole());
        
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testCreateUserWithExistingUsername() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(createUserRequest);
        });
        assertEquals("Username already exists", exception.getMessage());
        
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void testCreateUserWithExistingEmail() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(createUserRequest);
        });
        assertEquals("Email already exists", exception.getMessage());
        
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void testGetUserByIdSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        // Act
        UserInfoResponse response = userService.getUserById(1L);
        
        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("Test User", response.getFullName());
        
        verify(userRepository).findById(1L);
    }
    
    @Test
    void testGetUserByIdNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });
        assertEquals("User not found with id: 1", exception.getMessage());
        
        verify(userRepository).findById(1L);
    }
    
    @Test
    void testUpdateUserSuccess() {
        // Arrange
        CreateUserRequest updateRequest = new CreateUserRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setPassword("newpassword123");
        updateRequest.setFullName("Updated User");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setRole("ADMIN");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("updateduser")).thenReturn(false);
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(passwordEncoder.encode("newpassword123")).thenReturn("encoded_new_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act
        UserInfoResponse response = userService.updateUser(1L, updateRequest);
        
        // Assert
        assertNotNull(response);
        
        verify(userRepository).findById(1L);
        verify(userRepository).existsByUsername("updateduser");
        verify(userRepository).existsByEmail("updated@example.com");
        verify(passwordEncoder).encode("newpassword123");
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testDeleteUserSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act
        assertDoesNotThrow(() -> {
            userService.deleteUser(1L);
        });
        
        // Assert
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
        verify(authService).revokeUserTokens("testuser");
    }
    
    @Test
    void testDeleteUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(1L);
        });
        assertEquals("User not found with id: 1", exception.getMessage());
        
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
        verify(authService, never()).revokeUserTokens(any());
    }
    
    @Test
    void testForceLogoutUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        // Act
        assertDoesNotThrow(() -> {
            userService.forceLogoutUser("testuser");
        });
        
        // Assert
        verify(userRepository).findByUsername("testuser");
        verify(authService).revokeUserTokens("testuser");
    }
    
    @Test
    void testForceLogoutUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.forceLogoutUser("nonexistent");
        });
        assertEquals("User not found with username: nonexistent", exception.getMessage());
        
        verify(userRepository).findByUsername("nonexistent");
        verify(authService, never()).revokeUserTokens(any());
    }
}