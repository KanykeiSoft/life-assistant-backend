package com.aiassistant.life_backend.controller;


import com.aiassistant.life_backend.dto.UserProfileRequest;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.model.UserProfile;
import com.aiassistant.life_backend.service.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }
    @PostMapping
    public UserProfile createOrUpdate(@RequestBody UserProfileRequest dto, Authentication auth){
        String email = auth.getName();
        return userProfileService.createOrUpdate(email, dto);

    }
    @GetMapping("/me")
    public UserProfile getMyProfile(Authentication auth){
        String email = auth.getName();
        return userProfileService.getMyProfile(email);
    }
}
