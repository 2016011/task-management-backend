package com.example.taskmanagementback.controller;

import com.example.taskmanagementback.dto.UserLoginDto;
import com.example.taskmanagementback.modals.User;
import com.example.taskmanagementback.modals.UserLoginResult;
import com.example.taskmanagementback.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.taskmanagementback.util.TokenUtil.generateRefreshToken;
import static com.example.taskmanagementback.util.TokenUtil.generateToken;

@Component
public class LoginHelper {
    @Autowired
    UserService userService;
    private static final String SUCCESS_RESPONSE = "Success";
    private static final Logger logger = LogManager.getLogger(LoginHelper.class);

    public LoginHelper(UserService userService){
        this.userService = userService;
    }

    public UserLoginResult login(Authentication authentication, UserLoginDto userLoginDto){
        Optional<User> user = userService.findUserByUsername(userLoginDto.getUserName());
        return getGoProLoginResults(authentication, user);
    }

    public UserLoginResult unlockUser(Authentication authentication, User user){
        return getGoProLoginResults(authentication, Optional.ofNullable(user));
    }

    private UserLoginResult getGoProLoginResults(Authentication authentication, Optional<User> user) {
        if (authentication.isAuthenticated()) {
            String token = generateToken(user.get().getUserId());
            String refreshToken = generateRefreshToken(user.get().getUserId());

            logger.info("UserLogin Admin id: {} token: {} ", user.get().getUserId(), token);
            return new UserLoginResult(token, refreshToken,user.get().getUserId(),SUCCESS_RESPONSE);
        }else{
            logger.info("UserLogin Incorrect username or password");
            return new UserLoginResult(null, null, null,"Incorrect username or password");
        }
    }
}
