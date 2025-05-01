package com.dju.lounge.domain.post.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts {

    @Id
    @Tsid
    private String id;

    private String title;
    private String content;
    private String userId;
    private String categorySlug;

    @ElementCollection
    private List<String> tags;

    @ElementCollection
    private List<String> imageUrls;

    private String thumbnailUrl;

    private LocalDateTime createdAt;
    private boolean isUpdated;

    private int viewCount;
    private int likeCount;
    private int commentCount;
    private int bookmarkCount;

    @Builder
    public Posts(String title, String content, String userId, String categorySlug, String thumbnailUrl) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.categorySlug = categorySlug;
        this.tags = new ArrayList<>();
        this.imageUrls = new ArrayList<>();
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = LocalDateTime.now();
        this.isUpdated = false;
        this.viewCount = 0;
        this.likeCount = 0;
        this.commentCount = 0;
        this.bookmarkCount = 0;
    }

    public void updatePost(String title, String content, List<String> tags, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.imageUrls = imageUrls;
    }
}
