package com.dju.lounge.domain.chat.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @Tsid
    private String id;

    private String chatRoomId;
    private String userId;
    private String content;

    private LocalDateTime createdAt;

    @Builder
    public ChatMessage(String chatRoomId, String userId, String content) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
