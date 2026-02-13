package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.RefreshTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RefreshTokenEntityRepositoryTest {
    private final static UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");
    private final static List<RefreshTokenEntity> REFRESH_TOKENS = new ArrayList<>();

    @Autowired
    private UserInfoEntityRepository userInfoEntityRepository;

    @Autowired
    private RefreshTokenEntityRepository refreshTokenEntityRepository;

    @BeforeEach
    void setUp() {
        userInfoEntityRepository.save(TEST_USER);
        RefreshTokenEntity newToken = TestDataGenerator.refreshTokenEntity("test-token", TEST_USER);
        REFRESH_TOKENS.add(refreshTokenEntityRepository.save(newToken));
    }

    @AfterEach
    void tearDown() {
        refreshTokenEntityRepository.deleteAll();
        userInfoEntityRepository.deleteAll();
        REFRESH_TOKENS.clear();
    }

    @DisplayName("특정 token 값에 대한 refresh_token Entity 조회")
    @Test
    void test_findByToken() {
        RefreshTokenEntity expected = REFRESH_TOKENS.get(0);
        String token = expected.getToken();
        RefreshTokenEntity actual = refreshTokenEntityRepository.findByToken(token).orElseThrow();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("특정 일자 기준으로 마지막 수정일자가 해당 일자 이전인 refresh_token Entity 삭제")
    @Test
    void test_deleteAllByModifiedAtBefore() {
        RefreshTokenEntity lastWeekToken = TestDataGenerator.refreshTokenEntity("refresh-token1", TEST_USER);

        LocalDateTime lastWeekDateTime = LocalDateTime.now().minusDays(8L);
        lastWeekToken.setModifiedAt(lastWeekDateTime);

        refreshTokenEntityRepository.save(lastWeekToken);

        long initRows = refreshTokenEntityRepository.count();
        long lastWeekClearRows = refreshTokenEntityRepository.deleteAllByModifiedAtBefore(LocalDateTime.now().minusWeeks(1L));

        assertThat(initRows).isEqualTo(2L);
        assertThat(lastWeekClearRows).isEqualTo(1L);
    }
}