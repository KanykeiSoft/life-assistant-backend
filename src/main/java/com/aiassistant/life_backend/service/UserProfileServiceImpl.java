package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.dto.UserProfileRequest;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.model.UserProfile;
import com.aiassistant.life_backend.repository.UserProfileRepository;
import com.aiassistant.life_backend.repository.UserRepository;

public class UserProfileServiceImpl implements UserProfileService{

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfile createOrUpdate(String email, UserProfileRequest dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);   // привязка к юзеру
                    return p;
                });


        return null;
    }

    @Override
    public UserProfile getMyProfile(String email) {
        return null;
    }
}
