package com.dju.lounge.domain.chat.repository;

import com.dju.lounge.domain.chat.model.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PrivateChatRepository extends JpaRepository<PrivateChat, String> {

    @Query("SELECT p FROM PrivateChat p WHERE (p.user1Id = :user1Id AND p.user2Id = :user2Id) OR (p.user1Id = :user2Id AND p.user2Id = :user1Id)")
    Optional<PrivateChat> findPrivateChatByUsers(@Param("user1Id") String user1Id, @Param("user2Id") String user2Id);

}
