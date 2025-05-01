package com.dju.lounge.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateGroupChatDto {

    private String name;
    private String description;
    private String tags;

    @Builder
    public CreateGroupChatDto(String name, String description, String tags) {
        this.name = name;
        this.description = description;
        this.tags = tags;
    }
}
