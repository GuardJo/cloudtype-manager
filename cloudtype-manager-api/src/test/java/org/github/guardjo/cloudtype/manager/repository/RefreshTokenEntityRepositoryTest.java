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
        RefreshTokenEntity newRefreshToken = TestDataGenerator.refreshTokenEntity("refresh-token1", TEST_USER);

        refreshTokenEntityRepository.save(newRefreshToken);

        long initRows = refreshTokenEntityRepository.count();
        int clearRows = refreshTokenEntityRepository.deleteAllByModifiedAtBefore(newRefreshToken.getModifiedAt());

        assertThat(initRows).isEqualTo(2L);
        assertThat(clearRows).isEqualTo(1);
    }

    @DisplayName("특정 토큰에 해당하는 refresh_token Entity 삭제")
    @Test
    void test_deleteAllByToken() {
        RefreshTokenEntity newRefreshToken = TestDataGenerator.refreshTokenEntity("refresh-token1", TEST_USER);

        refreshTokenEntityRepository.save(newRefreshToken);

        long totalCount = refreshTokenEntityRepository.count();
        int deletedRow = refreshTokenEntityRepository.deleteAllByToken(newRefreshToken.getToken());

        assertThat(totalCount).isNotZero();
        assertThat(deletedRow).isEqualTo(1);
        assertThat(refreshTokenEntityRepository.count()).isEqualTo(totalCount - deletedRow);
    }
}