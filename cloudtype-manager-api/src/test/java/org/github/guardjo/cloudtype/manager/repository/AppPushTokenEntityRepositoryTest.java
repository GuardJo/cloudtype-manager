package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.AppPushTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AppPushTokenEntityRepositoryTest {
    private final UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");

    @Autowired
    private UserInfoEntityRepository userInfoRepository;

    @Autowired
    private AppPushTokenEntityRepository appPushTokenRepository;

    @BeforeEach
    void setUp() {
        userInfoRepository.save(TEST_USER);
    }

    @AfterEach
    void tearDown() {
        userInfoRepository.deleteAll();
    }

    @DisplayName("신규 APP_PUSH_TOKEN 저장 테스트")
    @Test
    void test_saveNewToken() {
        String newToken = "test-token";
        String username = TEST_USER.getUsername();

        AppPushTokenEntity newEntity = AppPushTokenEntity.builder()
                .token(newToken)
                .device("WEB")
                .userInfo(TEST_USER)
                .build();

        AppPushTokenEntity actual = appPushTokenRepository.save(newEntity);
        long totalCount = appPushTokenRepository.count();

        assertThat(actual).isNotNull();
        assertThat(actual.getUserInfo()).isNotNull();
        assertThat(actual.getUserInfo().getUsername()).isEqualTo(username);
        assertThat(actual).isEqualTo(newEntity);
        assertThat(totalCount).isEqualTo(1L);
    }

    @DisplayName("토큰 값을 기준으로 AppPushToken Entity 조회")
    @Test
    void test_findByToken() {
        String newToken = "test-token";
        String username = TEST_USER.getUsername();

        AppPushTokenEntity expected = AppPushTokenEntity.builder()
                .token(newToken)
                .device("WEB")
                .userInfo(TEST_USER)
                .build();

        expected = appPushTokenRepository.save(expected);
        appPushTokenRepository.flush();

        AppPushTokenEntity actual = appPushTokenRepository.findByToken(newToken).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }
}