package com.dju.lounge.global.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private boolean isSignUp;
}
