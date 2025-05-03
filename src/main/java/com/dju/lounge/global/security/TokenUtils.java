package com.dju.lounge.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private static Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    private TokenUtils() {
    }

    public static String extractTokenFromRequest(HttpServletRequest request, String tokenType) {

        if (tokenType.equals("access_token")) {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                return token;
            }

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("access_token")) {
                        return cookie.getValue();
                    }
                }
            }
        } else if (tokenType.equals("refresh_token")) {
            String token = request.getHeader("Refresh-Token");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                return token;
            }

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("refresh_token")) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return null;
    }

    public static String getUserIdFromToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody();

            String userId = claims.getSubject();

            if (userId == null) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

            return userId;
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    public static String getClientTypeFromToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (token == null || token.isEmpty()) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody();

            return claims.get("client_type").toString();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    public static Cookie createCookie(String name, String token) {
        if (name.equals("access_token")) {
            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(7200);

            return cookie;
        } else {
            Cookie cookie = new Cookie("refresh_token", token);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);

            return cookie;
        }
    }
}
