package com.example.knowtify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.knowtify.exception.UserNotFoundException;
import com.example.knowtify.model.User;
import com.example.knowtify.repository.UserRepository;
import com.example.knowtify.security.JwtUtil;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ✅ User Registration (Save to MongoDB)
    public User registerUser(User user) {
        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // ✅ Login User & Generate JWT Token
    public String loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserNotFoundException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getUsername());
    }

    // ✅ Get User From JWT Token
    public User getUserFromToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new UserNotFoundException("No authentication token found.");
        }

        // Extract username from token
        Optional<String> extractedUsername = jwtUtil.extractUsername(token);
        if (extractedUsername.isEmpty()) {
            throw new UserNotFoundException("Invalid or expired token.");
        }

        // Fetch user from MongoDB
        return userRepository.findByUsername(extractedUsername.get())
                .orElseThrow(() -> new UserNotFoundException("User not found for token."));
    }

    // ✅ Update User Profile
    public User updateUser(String id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));

        return userRepository.save(user);
    }

    // ✅ Delete User By ID
    public void deleteUserById(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

}
