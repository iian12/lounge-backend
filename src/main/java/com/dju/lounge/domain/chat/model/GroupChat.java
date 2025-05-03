package com.dju.lounge.domain.chat.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("GROUP")
public class GroupChat extends Chat {

    private String name;
    private String description;
    private String ownerId;
    private List<String> userIds;
    private int userCount;

    @Builder
    public GroupChat(String name, String description, String ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.userIds = new ArrayList<>();
        this.userCount = 1;
    }

    public void addUserId(String userId) {
        this.userIds.add(userId);
        this.userCount++;
    }

    public void updateGroupTopic(String groupTopic) {
        this.name = groupTopic;
    }
}
