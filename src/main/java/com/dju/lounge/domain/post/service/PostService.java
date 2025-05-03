package com.dju.lounge.domain.post.service;

import com.dju.lounge.domain.post.dto.FileUploadResult;
import com.dju.lounge.domain.post.dto.PostReqDto;
import com.dju.lounge.domain.post.dto.PostWithVoteReqDto;
import com.dju.lounge.domain.post.model.PostCategory;
import com.dju.lounge.domain.post.model.Posts;
import com.dju.lounge.domain.post.model.VoteOption;
import com.dju.lounge.domain.post.model.Votes;
import com.dju.lounge.domain.post.repository.PostCategoryRepository;
import com.dju.lounge.domain.post.repository.PostRepository;
import com.dju.lounge.domain.post.repository.VoteOptionRepository;
import com.dju.lounge.domain.user.model.Users;
import com.dju.lounge.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final FileService fileService;
    private final PostCategoryRepository postCategoryRepository;
    private final HashtagService hashtagService;

    public PostService(UserRepository userRepository, PostRepository postRepository,
        VoteOptionRepository voteOptionRepository, FileService fileService,
        PostCategoryRepository postCategoryRepository, HashtagService hashtagService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.voteOptionRepository = voteOptionRepository;
        this.fileService = fileService;
        this.postCategoryRepository = postCategoryRepository;
        this.hashtagService = hashtagService;
    }

    public String createPost(PostReqDto postReqDto) {
        Users user = userRepository.findById(
                SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Posts post = createBasePost(postReqDto, user);
        postRepository.save(post);

        return post.getId();
    }

    public String createPostWithVote(PostWithVoteReqDto reqDto) {
        Users user = userRepository.findById(
                SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Posts post = createBasePost(reqDto, user);
        postRepository.save(post);

        createVote(reqDto, post.getId());

        return post.getId();
    }

    private Posts createBasePost(PostReqDto reqDto, Users user) {
        List<String> tags = hashtagService.findOrCreateHashtags(reqDto.getHashtagNames());
        FileUploadResult result = fileService.validateAndSaveFilesWithThumbnailIncluded(
            reqDto.getFiles());
        List<String> fileUrls = result.fileUrls();
        String thumbnailUrl = result.thumbnailUrl();

        PostCategory category = postCategoryRepository.findById(reqDto.getCategory())
            .orElseThrow(() -> new EntityNotFoundException("PostCategory not found"));
        if (!category.isActive()) {
            throw new IllegalArgumentException("PostCategory is not active");
        }

        return Posts.builder().title(reqDto.getTitle()).content(reqDto.getContent())
            .userId(user.getId()).categorySlug(category.getSlug()).thumbnailUrl(thumbnailUrl)
            .tags(tags).fileUrls(fileUrls).build();
    }

    private void createVote(PostWithVoteReqDto reqDto, String postId) {
        Votes vote = Votes.builder().postId(postId).question(reqDto.getQuestion())
            .multipleChoice(reqDto.isMultipleChoice()).build();

        List<VoteOption> voteOptions = reqDto.getOptions().stream()
            .map(option -> VoteOption.builder().voteId(vote.getId()).content(option).build())
            .toList();
        voteOptionRepository.saveAll(voteOptions);
    }
}
