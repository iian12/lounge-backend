package com.dju.lounge.domain.post.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostWithVoteReqDto extends PostReqDto {

    private boolean multipleChoice;
    private String question;
    private List<String> options;

}
