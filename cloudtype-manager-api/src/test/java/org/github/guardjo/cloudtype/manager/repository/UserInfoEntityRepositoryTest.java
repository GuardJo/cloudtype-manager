package org.github.guardjo.cloudtype.manager.repository;

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
class UserInfoEntityRepositoryTest {
    private final List<UserInfoEntity> userInfoEntities = new ArrayList<>();

    @Autowired
    private UserInfoEntityRepository userInfoEntityRepository;

    @BeforeEach
    void setUp() {
        UserInfoEntity testUser = TestDataGenerator.userInfoEntity("Tester");

        userInfoEntities.add(userInfoEntityRepository.save(testUser));
    }

    @AfterEach
    void tearDown() {
        userInfoEntities.clear();
    }

    @DisplayName("ID를 기반으로 하여 특정 사용자 정보 조회")
    @Test
    void test_findById() {
        UserInfoEntity expected = userInfoEntities.get(0);

        UserInfoEntity actual = userInfoEntityRepository.findById(expected.getUsername()).orElseThrow();

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getCreatedAt()).isEqualTo(expected.getCreatedAt());
        assertThat(actual.getModifiedAt()).isEqualTo(expected.getModifiedAt());
        assertThat(actual.getCreatedAt()).isEqualTo(actual.getModifiedAt());
    }
}