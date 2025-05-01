package com.dju.lounge.domain.chat.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PRIVATE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateChat extends Chat {

    private String user1Id;
    private String user2Id;

    private String user1Nickname;
    private String user2Nickname;

    @Builder
    public PrivateChat(String user1Id, String user2Id, String user1Nickname, String user2Nickname) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.user1Nickname = user1Nickname;
        this.user2Nickname = user2Nickname;
    }
}
