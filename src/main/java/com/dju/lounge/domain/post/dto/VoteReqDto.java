package com.dju.lounge.domain.post.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteReqDto {

    List<String> selectedOptionIds;
}
