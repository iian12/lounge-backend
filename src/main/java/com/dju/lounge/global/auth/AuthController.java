package com.dju.lounge.global.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${client-id}")
    private String CLIENT_ID;

    @PostMapping("/google")
    public ResponseEntity<String> loginWithGoogle(@RequestBody IdTokenDto idToken, HttpServletResponse response) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY).setAudience(
                    Collections.singletonList(CLIENT_ID)).build();

            String cleanedToken = idToken.getIdToken();
            GoogleIdToken token = verifier.verify(cleanedToken);

            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();

                String email = payload.getEmail();
                String profileImgUrl = (String) payload.get("picture");
                String subjectId = payload.getSubject();

                GoogleUserDto googleUserDto = new GoogleUserDto();
                googleUserDto.setEmail(email);
                googleUserDto.setProfileImgUrl(profileImgUrl);
                googleUserDto.setSubjectId(subjectId);

                TokenResponseDto tokenResponseDto = authService.processingGoogleUser(googleUserDto);

                if (tokenResponseDto.isSignUp()) {
                    response.setHeader("Authorization", "Bearer " + tokenResponseDto.getAccessToken());
                    response.setHeader("Refresh-Token", tokenResponseDto.getRefreshToken());
                    return ResponseEntity.ok().build();
                } else {
                    response.setHeader("Authorization", "Bearer " + tokenResponseDto.getAccessToken());
                    response.setHeader("Refresh-Token", tokenResponseDto.getRefreshToken());
                    return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
