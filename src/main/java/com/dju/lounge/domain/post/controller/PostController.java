package com.dju.lounge.domain.post.controller;

import com.dju.lounge.domain.post.dto.PostReqDto;
import com.dju.lounge.domain.post.dto.PostWithVoteReqDto;
import com.dju.lounge.domain.post.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createPost(@ModelAttribute PostReqDto reqDto) {
        String postId;
        if (reqDto instanceof PostWithVoteReqDto) {
            postId = postService.createPostWithVote((PostWithVoteReqDto) reqDto);
        } else {
            postId = postService.createPost(reqDto);
        }

        return ResponseEntity.ok(postId);
    }
}
