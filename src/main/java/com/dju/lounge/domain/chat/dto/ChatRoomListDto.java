package com.dju.lounge.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomListDto {

    private String roomId;
    private String roomName;
    private int userCount;

    @Builder
    public ChatRoomListDto(String roomId, String roomName, int userCount) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.userCount = userCount;
    }
}
