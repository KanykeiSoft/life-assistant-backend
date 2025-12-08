package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.dto.UserProfileRequest;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.model.UserProfile;

public interface UserProfileService {
    UserProfile createOrUpdate(String email, UserProfileRequest dto);
    UserProfile getMyProfile(String email);
}
