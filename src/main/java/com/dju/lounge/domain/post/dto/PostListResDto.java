package com.dju.lounge.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostListResDto {

    private String id;
    private String title;
    private String content;
    private String category;
    private String createdAt;
    private String updatedAt;
    private String authorId;
    private String authorName;
}
