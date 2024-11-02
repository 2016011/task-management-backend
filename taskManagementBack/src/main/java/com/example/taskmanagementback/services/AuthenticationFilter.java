package com.example.taskmanagementback.services;

import com.auth0.jwt.JWT;
import com.example.taskmanagementback.modals.User;
import com.example.taskmanagementback.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@EnableWebSecurity
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final Logger authlogger = LogManager.getLogger(AuthenticationFilter.class);

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        authlogger.info("req path : {} auth: {}", httpServletRequest.getRequestURI(), httpServletRequest.getAuthType());
        final String tokenHeader = httpServletRequest.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            authlogger.info("tokenHeader : {}", tokenHeader);
            String authToken = tokenHeader.replace("Bearer ", "");

            try {
                JWT.require(TokenUtil.getSignAlgorithm()).build().verify(authToken);
                Long id = TokenUtil.getidFromToken(authToken);
                authlogger.info("id : {}", id);

                Optional<User> user = userService.findUserById(id);

                if (user.isEmpty()) {
                    authlogger.info("User not found");
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                } else {

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user.get().getEmail(), user.get().getPassword(), null);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    authlogger.info("Authentication : {}", authenticationToken.isAuthenticated());

                    authlogger.info("Authentication path : {}", httpServletRequest.getRequestURI());

                    if (!authenticationToken.isAuthenticated()) {
                        authlogger.info("Token Invalid");
                        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
                    }
                }
            } catch (Exception e) {
                authlogger.error(e.getMessage(),e);
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            }
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token Header Invalid");
            authlogger.error("Invalid header uri - {} token : {} " ,httpServletRequest.getRequestURI(), tokenHeader);
        }

        if (httpServletResponse.getStatus() == 200) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        boolean auth = request.getRequestURI().contains("/api/users/login") ||
                request.getRequestURI().contains("/api/users/create");
        logger.info("authentication filter returned : " + auth);
        return auth;
    }
}
