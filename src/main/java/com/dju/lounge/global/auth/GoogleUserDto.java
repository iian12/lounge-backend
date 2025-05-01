package com.dju.lounge.global.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleUserDto {

    private String email;
    private String profileImgUrl;
    private String subjectId;
}
