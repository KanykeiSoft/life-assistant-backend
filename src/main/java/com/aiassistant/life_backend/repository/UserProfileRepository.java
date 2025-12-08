package com.aiassistant.life_backend.repository;

import com.aiassistant.life_backend.model.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
    //Optional<UserProfile> findByUserEmail(String email);
}
