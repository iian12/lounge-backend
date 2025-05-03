package com.dju.lounge.domain.post.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Votes {

    public boolean multipleChoice;
    @Id
    @Tsid
    private String id;
    private String userId;
    private String postId;
    private String question;
    @ElementCollection
    private List<String> optionIds;
    private int totalVotes;

    @Builder
    public Votes(String userId, String postId, String question, List<String> optionIds,
        boolean multipleChoice) {
        this.userId = userId;
        this.postId = postId;
        this.question = question;
        this.optionIds = optionIds;
        this.multipleChoice = multipleChoice;
        this.totalVotes = 0;
    }
}
