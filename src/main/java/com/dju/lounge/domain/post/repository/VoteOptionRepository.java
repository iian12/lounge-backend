package com.dju.lounge.domain.post.repository;

import com.dju.lounge.domain.post.model.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteOptionRepository extends JpaRepository<VoteOption, String> {

}
