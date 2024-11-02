package com.example.taskmanagementback.controller;

import com.example.taskmanagementback.dto.UserLoginDto;
import com.example.taskmanagementback.modals.User;
import com.example.taskmanagementback.modals.UserLoginResult;
import com.example.taskmanagementback.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    LoginHelper loginHelper;

    private final UserService userService;

    private static final String INCORRECT_USERNAME_PASSWORD = "Incorrect username or password";

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        if (user.getUserId() != null) {
            return new ResponseEntity<>("User ID must be null for new user creation", HttpStatus.BAD_REQUEST);
        }

        try {
            User newUser = userService.saveOrUpdateUsers(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while creating the user. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // User login endpoint with enhanced error handling
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<UserLoginResult> userLogin(@RequestBody UserLoginDto userLoginDto) {
        logger.info("Attempting authentication for user: {}", userLoginDto.getUserName());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getUserName(), userLoginDto.getPassword())
            );
            UserLoginResult loginResult = loginHelper.login(authentication, userLoginDto);
            return new ResponseEntity<>(loginResult, HttpStatus.OK);

        } catch (BadCredentialsException badCredentialsException) {
            logger.warn("Invalid login attempt for user: {}", userLoginDto.getUserName());
            return new ResponseEntity<>(
                    new UserLoginResult(null, null, null, INCORRECT_USERNAME_PASSWORD),
                    HttpStatus.UNAUTHORIZED
            );

        } catch (Exception e) {
            logger.error("An error occurred during login: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new UserLoginResult(null, null, null, "An unexpected error occurred. Please try again."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
