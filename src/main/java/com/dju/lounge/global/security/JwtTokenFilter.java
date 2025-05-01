package com.dju.lounge.global.security;

import com.dju.lounge.global.auth.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider,
                          CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        if ((path.startsWith("/api/v1/posts/") && method.equals("GET")) ||
                path.startsWith("/login") ||
                path.startsWith("/images") ||
                path.startsWith("/api/v1/auth/google")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = TokenUtils.extractTokenFromRequest(request);

        if (token == null) {
            handleException(response, "MISSING_TOKEN", "Token is missing");
            return;
        }

        try {
            if (!jwtTokenProvider.validateToken(token)) {
                handleException(response, "INVALID_TOKEN", "Invalid token");
                return;
            }

            String userId = TokenUtils.getUserIdFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserById(userId);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            handleException(
                    response, "TOKEN_EXPIRED", "Token has expired");
            return;
        } catch (SignatureException e) {
            handleException(
                    response, "INVALID_SIGNATURE", "Token signature is invalid");
            return;
        } catch (Exception e) {
            handleException(
                    response, "TOKEN_ERROR", "An error occurred while processing the token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, String errorCode, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                String.format("{\"error_code\": \"%s\", \"message\": \"%s\"}", errorCode, message));
    }
}
