package com.dju.lounge.domain.post.repository;

import com.dju.lounge.domain.post.model.Hashtag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, String> {

    Optional<Hashtag> findByName(String name);
}
