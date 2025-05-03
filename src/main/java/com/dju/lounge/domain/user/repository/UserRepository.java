package com.dju.lounge.domain.user.repository;

import com.dju.lounge.domain.user.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, String> {

    Optional<Users> findByEmail(String email);
}
