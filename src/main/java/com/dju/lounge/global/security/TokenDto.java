package com.dju.lounge.global.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
