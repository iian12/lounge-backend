package com.dju.lounge.domain.chat.repository;

import com.dju.lounge.domain.chat.model.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(String roomId);
}
