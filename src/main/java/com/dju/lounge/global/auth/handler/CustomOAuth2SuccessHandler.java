package com.dju.lounge.global.auth.handler;

import com.dju.lounge.domain.user.model.Users;
import com.dju.lounge.domain.user.repository.UserRepository;
import com.dju.lounge.global.auth.CustomUserDetail;
import com.dju.lounge.global.config.ClientConfig;
import com.dju.lounge.global.security.JwtTokenProvider;
import com.dju.lounge.global.security.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public CustomOAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider,
        UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        String userId = userDetail.getUserId();

        String accessToken = jwtTokenProvider.createAccessToken(userId, ClientConfig.WEB);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, ClientConfig.WEB);
        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);
        Cookie accessTokenCookie = TokenUtils.createCookie("access_token", accessToken);
        Cookie refreshTokenCookie = TokenUtils.createCookie("refresh_token", refreshToken);

        Users user = userRepository.findById(userId).orElse(null);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        if (!Objects.requireNonNull(user).isSignUp()) {
            response.sendRedirect("http://localhost:3000/set-nickname");
        } else {
            response.sendRedirect("http://localhost:3000/");
        }
    }
}
