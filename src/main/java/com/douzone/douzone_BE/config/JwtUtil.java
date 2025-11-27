package com.douzone.douzone_BE.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "your-secret-key-should-be-long-enough-for-hs256"; // 충분히 긴 문자열
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24시간

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(String id, String role, String name) {
        return Jwts.builder()
            .claim("id", id)
            .claim("role", role)
            .claim("name", name)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
            .signWith(KEY) // Key 객체 사용
            .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
