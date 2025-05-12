package com.dju.lounge.domain.post.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategory {

    @Id
    private String slug;

    private String name;
    private int postCount;
    private boolean isActive;

    @Builder
    public PostCategory(String slug, String name) {
        this.slug = slug;
        this.name = name;
        this.postCount = 0;
        this.isActive = true;
    }

    public void addPostCount() {
        this.postCount++;
    }
}
