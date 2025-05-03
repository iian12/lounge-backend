package com.dju.lounge.domain.post.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String voteId;
    private String optionId;

    @Builder
    public UserVote(String userId, String voteId, String optionId) {
        this.userId = userId;
        this.voteId = voteId;
        this.optionId = optionId;
    }
}
