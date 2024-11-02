package com.example.taskmanagementback.services;

import com.example.taskmanagementback.modals.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    public UserDetails loadUserByUsername(String userName) {
        User user = this.userService.findUserByUsername(userName).orElseThrow(() ->
                new UsernameNotFoundException(userName + " not found"));
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = (User)this.userService.findUserById(id).orElseThrow(() -> {
            return new ResourceNotFoundException("User", "id", id);
        });
        return UserPrincipal.create(user);
    }
}
