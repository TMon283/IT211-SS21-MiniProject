package org.example.miniproj.service;

import org.example.miniproj.dto.request.CreateUserRequest;
import org.example.miniproj.dto.response.UserInfoResponse;
import org.example.miniproj.entity.User;
import org.example.miniproj.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthService authService;
    
    public UserInfoResponse createUser(CreateUserRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRoleEnum());
        
        User savedUser = userRepository.save(user);
        logger.info("User created successfully: {}", savedUser.getUsername());
        
        return new UserInfoResponse(savedUser);
    }
    
    public List<UserInfoResponse> getAllUsers() {
        return userRepository.findActiveUsers()
                .map(UserInfoResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<UserInfoResponse> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role)
                .map(UserInfoResponse::new)
                .collect(Collectors.toList());
    }
    
    public UserInfoResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return new UserInfoResponse(user);
    }
    
    public UserInfoResponse updateUser(Long id, CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Check if new username/email conflicts with existing users (excluding current user)
        if (!user.getUsername().equals(request.getUsername()) && 
            userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRoleEnum());
        
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully: {}", updatedUser.getUsername());
        
        return new UserInfoResponse(updatedUser);
    }
    
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setEnabled(false);
        userRepository.save(user);
        
        // Revoke all tokens for this user
        authService.revokeUserTokens(user.getUsername());
        
        logger.info("User deactivated successfully: {}", user.getUsername());
    }
    
    public void forceLogoutUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        authService.revokeUserTokens(username);
        logger.info("Force logout executed for user: {}", username);
    }
}