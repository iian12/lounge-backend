package com.dju.lounge.domain.user.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id
    @Tsid
    private String id;

    private String email;
    private String password;

    @Column(nullable = true, unique = true)
    private String nickname;
    private String profileImgUrl;
    private boolean isSignUp;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String subjectId;

    @Builder
    public Users(String email, String password, String nickname, String profileImgUrl,
        Provider provider, String subjectId, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.isSignUp = false;
        this.provider = provider;
        this.subjectId = subjectId;
        this.role = role;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateSignUp() {
        this.isSignUp = true;
    }
}
