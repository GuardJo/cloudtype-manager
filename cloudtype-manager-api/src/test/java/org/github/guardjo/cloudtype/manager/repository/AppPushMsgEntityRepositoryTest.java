package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.AppPushMsgEntity;
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
class AppPushMsgEntityRepositoryTest {
    private final UserInfoEntity testUser = TestDataGenerator.userInfoEntity("tester");
    private final AppPushTokenEntity testToken = TestDataGenerator.appPushTokenEntity("test-token", testUser);

    @Autowired
    private UserInfoEntityRepository userInfoRepository;

    @Autowired
    private AppPushTokenEntityRepository appPushTokenRepository;

    @Autowired
    private AppPushMsgEntityRepository appPushMsgRepository;

    @BeforeEach
    void setUp() {
        userInfoRepository.save(testUser);
        appPushTokenRepository.save(testToken);
    }

    @AfterEach
    void tearDown() {
        appPushMsgRepository.deleteAll();
        userInfoRepository.deleteAll();
    }

    @DisplayName("APP_PUSH_MSG 저장")
    @Test
    void test_save() {
        long totalCount = appPushMsgRepository.count();

        AppPushMsgEntity appPushMsg = AppPushMsgEntity.builder()
                .title("test-title")
                .body("test-body")
                .appPushToken(testToken)
                .build();

        appPushMsg = appPushMsgRepository.save(appPushMsg);

        AppPushMsgEntity actual = appPushMsgRepository.findById(appPushMsg.getId()).orElseThrow();
        long actualSize = appPushMsgRepository.count();

        assertThat(actual).isEqualTo(appPushMsg);
        assertThat(actualSize).isEqualTo(totalCount + 1);
    }
}