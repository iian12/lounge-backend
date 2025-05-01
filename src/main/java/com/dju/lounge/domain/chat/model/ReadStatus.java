package com.dju.lounge.domain.chat.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus {

    @Id
    @Tsid
    private String id;

    private String chatRoomId;
    private String userId;
    private String messageId;
    private boolean isRead;

    @Builder
    public ReadStatus(String chatRoomId, String userId, String messageId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.messageId = messageId;
        this.isRead = false;
    }

    public void updateReadStatus() {
        this.isRead = true;
    }
}
