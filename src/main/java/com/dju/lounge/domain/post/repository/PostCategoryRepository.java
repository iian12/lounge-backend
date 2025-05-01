package com.dju.lounge.domain.post.repository;

import com.dju.lounge.domain.post.model.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, String> {
}
