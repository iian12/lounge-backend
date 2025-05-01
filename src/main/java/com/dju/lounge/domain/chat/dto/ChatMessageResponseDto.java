package com.dju.lounge.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChatMessageResponseDto {

    private String message;
    private String senderNickname;
    private String senderProfileImgUrl;

    @Builder
    public ChatMessageResponseDto(String message, String senderNickname, String senderProfileImgUrl) {
        this.message = message;
        this.senderNickname = senderNickname;
        this.senderProfileImgUrl = senderProfileImgUrl;
    }
}
