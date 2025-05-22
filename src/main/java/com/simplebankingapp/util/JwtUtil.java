package com.simplebankingapp.util;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class JwtUtil {
//    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final String SECRET_STRING = "Apogee"; // your small secret
    private final SecretKey key = generateHashedKey(SECRET_STRING);
    private final long EXPIRATION = 360000; // 6 minutes

    private SecretKey generateHashedKey(String secret) {
        try {
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            byte[] hashed = sha512.digest(secret.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(hashed, SignatureAlgorithm.HS512.getJcaName());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 not supported", e);
        }
    }
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
