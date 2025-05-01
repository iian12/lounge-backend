package com.dju.lounge.domain.chat.repository;

import com.dju.lounge.domain.chat.model.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, String> {
}
