package com.dju.lounge.domain.chat.repository;

import com.dju.lounge.domain.chat.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatRepository extends JpaRepository<GroupChat, String> {

}
