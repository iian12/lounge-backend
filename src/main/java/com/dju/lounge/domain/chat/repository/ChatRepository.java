package com.dju.lounge.domain.chat.repository;

import com.dju.lounge.domain.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, String> {

}
