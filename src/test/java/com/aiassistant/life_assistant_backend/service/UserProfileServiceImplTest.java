package com.aiassistant.life_assistant_backend.service;

import com.aiassistant.life_backend.dto.UserProfileRequest;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.model.UserProfile;
import com.aiassistant.life_backend.repository.UserProfileRepository;
import com.aiassistant.life_backend.repository.UserRepository;
import com.aiassistant.life_backend.service.UserProfileServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    void createOrUpdate_shouldCreateProfileIfNotExists() {
        // given
        String email = "test@gmail.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        UserProfileRequest dto = new UserProfileRequest();
        dto.setAge(25);
        dto.setSleepHours(8);
        dto.setGoal("lose weight");

        // user найден по email
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // профиля ещё нет
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // при save(...) просто возвращаем тот объект, который передали
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when – вызываем реальный метод сервиса
        UserProfile result = userProfileService.createOrUpdate(email, dto);

        // then – проверяем, что всё заполнилось как нужно
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getAge()).isEqualTo(25);
        assertThat(result.getSleepHours()).isEqualTo(8);
        assertThat(result.getGoal()).isEqualTo("lose weight");

        // и что save действительно вызывался
        verify(userProfileRepository).save(any(UserProfile.class));

    }

    @Test
    void getMyProfile_shouldReturnProfile() {
        // given
        String email = "test@gmail.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        UserProfile profile = new UserProfile();
        profile.setId(10L);
        profile.setUser(user);
        profile.setAge(30);
        profile.setSleepHours(7);
        profile.setGoal("maintain weight");

        // user найден по email
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        // профиль найден по userId
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        // when
        UserProfile result = userProfileService.getMyProfile(email);

        // then — здесь используй тот же стиль assert, что и в первом тесте
        // пример с JUnit Assertions:
        // assertEquals(profile, result);
        // assertEquals(30, result.getAge());
        // assertEquals(7, result.getSleepHours());

        // или если уже используешь assertThat из AssertJ:
        // assertThat(result).isEqualTo(profile);
        // assertThat(result.getAge()).isEqualTo(30);
        // assertThat(result.getSleepHours()).isEqualTo(7);
    }

}