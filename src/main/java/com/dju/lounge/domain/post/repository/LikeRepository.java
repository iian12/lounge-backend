package com.dju.lounge.domain.post.repository;

import com.dju.lounge.domain.post.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
