package com.dju.lounge.domain.post.repository;

import com.dju.lounge.domain.post.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Posts, String> {

}
