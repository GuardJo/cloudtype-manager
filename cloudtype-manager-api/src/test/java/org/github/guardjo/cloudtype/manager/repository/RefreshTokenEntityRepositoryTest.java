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

    @DisplayName("특정 사용자에 대한 refresh_token Entity 조회")
    @Test
    void test_findByUserInfo_Username() {
        RefreshTokenEntity expected = REFRESH_TOKENS.get(0);
        RefreshTokenEntity actual = refreshTokenEntityRepository.findByUserInfo_Username(TEST_USER.getUsername()).orElseThrow();

        assertThat(actual).isEqualTo(expected);
    }
}