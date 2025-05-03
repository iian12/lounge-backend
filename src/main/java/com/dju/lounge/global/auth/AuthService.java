package com.dju.lounge.global.auth;

import com.dju.lounge.domain.user.model.Provider;
import com.dju.lounge.domain.user.model.Users;
import com.dju.lounge.domain.user.repository.UserRepository;
import com.dju.lounge.global.config.ClientConfig;
import com.dju.lounge.global.security.JwtTokenProvider;
import com.dju.lounge.global.security.TokenUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    @Value("${client-id}")
    private String CLIENT_ID;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public TokenResult processingGoogleUser(IdTokenDto idToken) {

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

                Optional<Users> user = userRepository.findByEmail(email);

                if (user.isPresent()) {
                    Users existingUser = user.get();
                    String userId = existingUser.getId();
                    if (existingUser.getNickname() == null) {
                        return generateTokens(userId, ClientConfig.APP, false);
                    } else {
                        return generateTokens(userId, ClientConfig.APP, true);
                    }
                } else {
                    Users newUser = Users.builder().email(email).provider(Provider.GOOGLE)
                        .profileImgUrl(profileImgUrl).subjectId(subjectId).build();

                    userRepository.save(newUser);
                    String userId = newUser.getId();
                    return generateTokens(userId, ClientConfig.APP, false);
                }
            } else {
                throw new IllegalArgumentException("Invalid ID token");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private TokenResult generateTokens(String userId, ClientConfig clientConfig, boolean isSignUp) {
        String accessToken = jwtTokenProvider.createAccessToken(userId, clientConfig);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, clientConfig);
        return new TokenResult(accessToken, refreshToken, clientConfig.toString(), isSignUp);
    }

    public TokenResult autoLogin(String token) {

        String clientType = TokenUtils.getClientTypeFromToken(token);
        String userId = TokenUtils.getUserIdFromToken(token);

        Optional<Users> user = userRepository.findById(userId);

        if (clientType.equals(ClientConfig.APP.toString())) {
            if (user.isPresent()) {
                if (user.get().getNickname() == null) {
                    return generateTokens(userId, ClientConfig.APP, false);
                } else {
                    return generateTokens(userId, ClientConfig.APP, true);
                }
            } else {
                throw new IllegalArgumentException("User not found");
            }
        } else {
            if (user.isPresent()) {
                if (user.get().getNickname() == null) {
                    return generateTokens(userId, ClientConfig.WEB, false);
                } else {
                    return generateTokens(userId, ClientConfig.WEB, true);
                }
            } else {
                throw new IllegalArgumentException("User not found");
            }
        }
    }
}
