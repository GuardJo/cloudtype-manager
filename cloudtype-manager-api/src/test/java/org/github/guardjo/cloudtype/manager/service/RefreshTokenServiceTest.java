package org.github.guardjo.cloudtype.manager.service;

import org.github.guardjo.cloudtype.manager.model.domain.RefreshTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.repository.RefreshTokenEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    private final static UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");
    private final static String TEST_TOKEN = "test-token";

    @Mock
    private UserInfoEntityRepository userInfoEntityRepository;

    @Mock
    private RefreshTokenEntityRepository refreshTokenEntityRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @DisplayName("주어진 계정 식별키에 대한 refresh_token 저장")
    @Test
    void test_saveNewToken() {
        String username = TEST_USER.getUsername();

        ArgumentCaptor<RefreshTokenEntity> tokenCaptor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        given(userInfoEntityRepository.getReferenceById(eq(username))).willReturn(TEST_USER);
        given(refreshTokenEntityRepository.save(tokenCaptor.capture())).willReturn(any(RefreshTokenEntity.class));

        assertThatCode(() -> refreshTokenService.saveNewToken(TEST_TOKEN, username)).doesNotThrowAnyException();
        RefreshTokenEntity actual = tokenCaptor.getValue();
        assertThat(actual.getToken()).isEqualTo(TEST_TOKEN);
        assertThat(actual.getUserInfo()).isEqualTo(TEST_USER);

        then(userInfoEntityRepository).should().getReferenceById(eq(username));
        then(refreshTokenEntityRepository).should().save(any(RefreshTokenEntity.class));
    }
}