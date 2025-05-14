package com.dju.lounge.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {

    @JsonIgnore
    private String roomId;
    private String messageId;
    private String message;

    @Builder
    public ChatMessageDto(String roomId, String messageId, String message) {
        this.roomId = roomId;
        this.messageId = messageId;
        this.message = message;
    }
}
