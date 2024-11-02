package com.example.taskmanagementback.services;

import com.example.taskmanagementback.modals.Tasks;
import com.example.taskmanagementback.modals.User;
import com.example.taskmanagementback.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    public User saveOrUpdateUsers(User user) {
        User userEntity = new User();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        if (user.getUserId()== null) {
            userEntity.setUsername(user.getUsername());
            userEntity.setEmail(user.getEmail());
            userEntity.setPassword(encodedPassword);
        } else {
            // Update the existing user from the database
            userEntity = userRepository.findByUserId(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (user.getUsername() != null) userEntity.setUsername(user.getUsername());
            if (user.getEmail() != null) userEntity.setEmail(user.getEmail());
            if (user.getPassword() != null) userEntity.setPassword(user.getPassword());
        }
        return userRepository.save(userEntity);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

}
