package com.dju.lounge.global.auth;

import com.dju.lounge.domain.user.model.Provider;
import com.dju.lounge.domain.user.model.Users;
import com.dju.lounge.domain.user.repository.UserRepository;
import com.dju.lounge.global.config.ClientConfig;
import com.dju.lounge.global.security.JwtTokenProvider;
import com.dju.lounge.global.security.TokenDto;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public TokenResponseDto processingGoogleUser(GoogleUserDto googleUserDto) {
        Optional<Users> user = userRepository.findByEmail(googleUserDto.getEmail());
        TokenResponseDto tokenResponseDto = new TokenResponseDto();

        if (user.isPresent()) {
            Users existingUser = user.get();

            if (existingUser.getNickname() == null) {
                String userId = existingUser.getId();
                generateTokens(tokenResponseDto, userId, ClientConfig.APP);
                tokenResponseDto.setSignUp(false);
            } else {
                String userId = existingUser.getId();
                generateTokens(tokenResponseDto, userId, ClientConfig.APP);
                tokenResponseDto.setSignUp(true);
            }
        } else {
            Users newUser = Users.builder()
                    .email(googleUserDto.getEmail())
                    .provider(Provider.GOOGLE)
                    .profileImgUrl(googleUserDto.getProfileImgUrl())
                    .subjectId(googleUserDto.getSubjectId())
                    .build();

            userRepository.save(newUser);
            String userId = newUser.getId();
            generateTokens(tokenResponseDto, userId, ClientConfig.APP);
            tokenResponseDto.setSignUp(false);
        }

        return tokenResponseDto;
    }

    private void generateTokens(TokenResponseDto tokenResponseDto, String userId, ClientConfig clientConfig) {
        String accessToken = jwtTokenProvider.createAccessToken(userId, clientConfig);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, clientConfig);
        tokenResponseDto.setAccessToken(accessToken);
        tokenResponseDto.setRefreshToken(refreshToken);
    }
}
