package com.dju.lounge.domain.post.dto;

import java.util.List;

public record FileUploadResult(String thumbnailUrl, List<String> fileUrls) {

}

