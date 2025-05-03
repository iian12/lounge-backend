package com.dju.lounge.global.auth;

public record TokenResult(String clientType, String accessToken, String refreshToken,
                          boolean isSignUp) {

}
