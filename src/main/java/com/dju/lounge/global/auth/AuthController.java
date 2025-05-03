package com.dju.lounge.global.auth;

import com.dju.lounge.global.config.ClientConfig;
import com.dju.lounge.global.security.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google-login-app")
    public ResponseEntity<?> loginWithGoogleOnAPP(@RequestBody IdTokenDto idToken,
        HttpServletResponse response) {
        try {
            TokenResult tokenResult = authService.processingGoogleUser(idToken);

            String accessToken = tokenResult.accessToken();
            String refreshToken = tokenResult.refreshToken();

            response.setHeader("Authorization", "Bearer " + accessToken);
            response.setHeader("Refresh-Token", refreshToken);

            return ResponseEntity.ok().body(tokenResult.isSignUp());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing Google token");
        }
    }

    @GetMapping("/auto-login")
    public ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = TokenUtils.extractTokenFromRequest(request, "refresh_token");
            TokenResult tokenResult = authService.autoLogin(token);

            if (tokenResult.clientType().equals(ClientConfig.WEB.toString())) {
                response.addCookie(
                    TokenUtils.createCookie("access_token", tokenResult.accessToken()));
                response.addCookie(
                    TokenUtils.createCookie("refresh_token", tokenResult.refreshToken()));
            } else {
                response.setHeader("Authorization", "Bearer " + tokenResult.accessToken());
                response.setHeader("Refresh-Token", tokenResult.refreshToken());
            }
            return ResponseEntity.ok().body(tokenResult.isSignUp());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
