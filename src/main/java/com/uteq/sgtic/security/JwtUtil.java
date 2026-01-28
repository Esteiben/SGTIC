package com.uteq.sgtic.security;

import com.uteq.sgtic.entities.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "sgtic_secret_key_2024";
    private final long EXPIRATION = 1000 * 60 * 60 * 8; // 8 horas

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getIdUser())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
