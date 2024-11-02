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
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getUserId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Id should be null for new user
        }
        User newUser = userService.saveOrUpdateUsers(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/login")
    public UserLoginResult userLogin(@RequestBody UserLoginDto userLoginDto){
        logger.info("goProUserLogin Attempting authentication");
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUserName(), userLoginDto.getPassword()));
            return loginHelper.login(authentication, userLoginDto);
        } catch (BadCredentialsException badCredentialsException) {
            logger.error("taskAppUserLogin badCredentialsException : {}", badCredentialsException.getMessage());
            badCredentialsException.printStackTrace();
            logger.error("taskAppUserLogin Authentication for {} failed", userLoginDto.getUserName());
            return new UserLoginResult(null,null,null,INCORRECT_USERNAME_PASSWORD);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new UserLoginResult(null,null,null,e.getMessage());
        }
    }
}
