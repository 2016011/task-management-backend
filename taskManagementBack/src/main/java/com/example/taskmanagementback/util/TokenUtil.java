package com.example.taskmanagementback.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class TokenUtil {
    private static final long TOKEN_VALIDITY =  60 * 60L;
    private static final long REFRESH_TOKEN_VALIDITY = 24 * 60 * 60L;

    private static Algorithm algorithm = Algorithm.HMAC256("myString");

    private static Algorithm refreshTokenAlgorithm = Algorithm.HMAC256("refreshTokenString");

    public TokenUtil() {
    }

    public static String generateToken(Long id) {
        return JWT.create().withIssuer("base_auth").withClaim("id", id).withIssuedAt(new Date(System.currentTimeMillis())).withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000)).sign(algorithm);
    }

    public static String generateRefreshToken(Long id){
        return JWT.create()
                .withIssuer("base_auth")
                .withClaim("id", id)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY * 1000))
                .sign(refreshTokenAlgorithm);
    }

    public static Long getidFromToken(String token) {
        System.out.println("getidFromToken start");
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
        return decodedJWT.getClaim("id").asLong();
    }

    public static Algorithm getSignAlgorithm() {
        return algorithm;
    }

    public static Long getidFromHeader(String header) {
        String token = header.split(" ")[1];
        return getidFromToken(token);
    }

    public static Long getidFromRefreshToken(String refreshToken) {
        System.out.println("getidFromRefreshToken start : " + refreshToken);
        DecodedJWT decodedJWT = JWT.require(refreshTokenAlgorithm).build().verify(refreshToken);
        Long id =  decodedJWT.getClaim("id").asLong();
        System.out.println("getidFromRefreshToken id : " + id);
        return id;
    }

    public static String refreshRefreshToken(String refreshToken) {
        System.out.println("refreshRefreshToken start : " + refreshToken);
        String token = JWT.create()
                .withIssuer("base_auth")
                .withClaim("id", getidFromRefreshToken(refreshToken))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY * 1000))
                .sign(refreshTokenAlgorithm);
        System.out.println("refreshRefreshToken new refreshToken : " + token);
        return  token;
    }

    public static String refreshToken(String token) {
        return JWT.create()
                .withIssuer("base_auth")
                .withClaim("id", getidFromToken(token))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .sign(algorithm);
    }
}
