package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
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
class ServerInfoEntityRepositoryTest {
    private final static UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");
    private final List<ServerInfoEntity> serverInfos = new ArrayList<>();

    @Autowired
    private ServerInfoEntityRepository serverInfoRepository;

    @Autowired
    private UserInfoEntityRepository userInfoEntityRepository;

    @BeforeEach
    void setUp() {
        userInfoEntityRepository.save(TEST_USER);
        for (int i = 0; i < 5; i++) {
            serverInfos.add(TestDataGenerator.serverInfoEntity("Server " + i, TEST_USER));
        }

        serverInfoRepository.saveAll(serverInfos);
    }

    @AfterEach
    void tearDown() {
        serverInfoRepository.deleteAll();
        serverInfos.clear();
        userInfoEntityRepository.deleteAll();
    }

    @DisplayName("특정 server_info 조회")
    @Test
    void test_findById() {
        ServerInfoEntity expected = serverInfos.get(0);

        ServerInfoEntity actual = serverInfoRepository.findById(expected.getId()).orElseThrow();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("특정 user_info에 연관된 server_info 목록 조회")
    @Test
    void test_findAllByUserInfo_Username() {
        List<ServerInfoEntity> actual = serverInfoRepository.findAllByUserInfo_Username(TEST_USER.getUsername());

        assertThat(actual).isEqualTo(serverInfos);
    }

    @DisplayName("특정 계정의 특정 server_info 조회")
    @Test
    void test_findByIdAndUserInfo_Username() {
        ServerInfoEntity expected = serverInfos.get(0);
        Long serverId = expected.getId();
        String userInfoId = expected.getUserInfo().getUsername();

        ServerInfoEntity actual = serverInfoRepository.findByIdAndUserInfo_Username(serverId, userInfoId).orElseThrow();

        assertThat(actual).isEqualTo(expected);
    }
}