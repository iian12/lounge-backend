package com.dju.lounge.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {

    private String roomId;
    private String message;

    @Builder
    public ChatMessageDto(String roomId, String message) {
        this.roomId = roomId;
        this.message = message;
    }
}
