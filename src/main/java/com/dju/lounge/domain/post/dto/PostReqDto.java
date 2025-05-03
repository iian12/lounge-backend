package com.dju.lounge.domain.post.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class PostReqDto {

    private String title;
    private String content;
    private String category;
    private List<String> hashtagNames = new ArrayList<>();
    private List<MultipartFile> files = new ArrayList<>();

    public PostReqDto(String title, String content, String category, List<String> hashtagNames,
        List<MultipartFile> files) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.hashtagNames = hashtagNames;
        this.files = files;
    }
}
