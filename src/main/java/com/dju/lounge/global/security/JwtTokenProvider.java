package com.dju.lounge.global.security;

import com.dju.lounge.domain.user.model.Users;
import com.dju.lounge.domain.user.repository.UserRepository;
import com.dju.lounge.global.config.ClientConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final UserRepository userRepository;

    public JwtTokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-ms}")
    private int accessTokenValidityInMs;

    @Value("${jwt.refresh-token-validity-in-ms}")
    private int refreshTokenValidityInMs;

    private Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String userId, ClientConfig clientConfig) {
        try {
            if (userId == null)
                throw new IllegalArgumentException("userId cannot be null");

            Map<String, Object> claims = new HashMap<>();

            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            claims.put("role", user.getRole());
            claims.put("clientType", clientConfig.toString());
            Date now = new Date();
            Date validity = new Date(now.getTime() + accessTokenValidityInMs);

            return Jwts.builder()
                    .setSubject(userId)
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createRefreshToken(String userId, ClientConfig clientConfig) {
        try {
            Date now = new Date();
            Date validity = new Date(now.getTime() + refreshTokenValidityInMs);

            Map<String, Object> claims = new HashMap<>();

            claims.put("client_type", clientConfig.toString());

            return Jwts.builder()
                    .setSubject(userId)
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateToken(String token) {
        if (token.startsWith("Bearer "))
            token = token.substring(7);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (SignatureException | MalformedJwtException | IllegalArgumentException |
                 ExpiredJwtException e) {
            return false;
        }
    }

}
