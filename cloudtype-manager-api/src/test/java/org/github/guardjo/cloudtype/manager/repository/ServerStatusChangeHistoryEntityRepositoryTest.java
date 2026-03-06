package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.ServerStatusChangeHistoryEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ServerStatusChangeHistoryEntityRepositoryTest {
    private final static Logger log = LoggerFactory.getLogger(ServerStatusChangeHistoryEntityRepositoryTest.class);

    private final UserInfoEntity testUserInfo = TestDataGenerator.userInfoEntity("tester");
    private final List<ServerStatusChangeHistoryEntity> serverStatusChangeHistories = new ArrayList<>();

    @Autowired
    private UserInfoEntityRepository userInfoRepository;

    @Autowired
    private ServerInfoEntityRepository serverInfoRepository;

    @Autowired
    private ServerStatusChangeHistoryEntityRepository serverStatusChangeHistoryRepository;

    @BeforeEach
    void setUp() {
        userInfoRepository.save(testUserInfo);
        ServerInfoEntity testServer = initServerInfo(testUserInfo, "Test Server");
        ServerStatusChangeHistoryEntity serverStatusChangeHistoryEntity = serverStatusChangeHistoryRepository.save(ServerStatusChangeHistoryEntity.builder()
                .server(testServer)
                .statusCode(400)
                .errorCategory("BAD_REQUEST")
                .build());
        serverStatusChangeHistories.add(serverStatusChangeHistoryEntity);
    }

    @AfterEach
    void tearDown() {
        serverStatusChangeHistoryRepository.deleteAll();
        serverStatusChangeHistories.clear();
        serverInfoRepository.deleteAll();
        userInfoRepository.deleteAll();
    }

    ServerInfoEntity initServerInfo(UserInfoEntity userInfo, String serverName) {
        ServerInfoEntity testServer = TestDataGenerator.serverInfoEntity(serverName, userInfo);
        serverInfoRepository.save(testServer);

        return testServer;
    }

    @DisplayName("특정 ServerStatusChangeHistoryEntity 조회")
    @Test
    void test_findById() {
        ServerStatusChangeHistoryEntity expected = serverStatusChangeHistories.get(0);
        Long id = expected.getId();

        ServerStatusChangeHistoryEntity actual = serverStatusChangeHistoryRepository.findById(id).orElseThrow();

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("특정 회원이 지닌 서버의 ServerStatusChangeHistoryEntity 목록 페이지네이션 조회")
    @Test
    void test_findAllByServer_UserInfo_Username() {
        log.info("test_findAllByServer_UserInfo_Username: Start");
        UserInfoEntity userInfo = TestDataGenerator.userInfoEntity("test2");
        userInfoRepository.save(userInfo);

        ServerInfoEntity serverInfo = TestDataGenerator.serverInfoEntity("test-server2", userInfo);
        serverInfoRepository.save(serverInfo);

        ServerStatusChangeHistoryEntity serverStatusChangeHistory = ServerStatusChangeHistoryEntity.builder()
                .server(serverInfo)
                .statusCode(400)
                .errorCategory("BAD_REQUEST")
                .build();

        serverStatusChangeHistoryRepository.save(serverStatusChangeHistory);

        long totalHistoryCount = serverStatusChangeHistoryRepository.count();

        assertThat(totalHistoryCount).isEqualTo(serverStatusChangeHistories.size() + 1);

        String testUserId = userInfo.getUsername();
        log.info("findAllByServer_UserInfo_Username()1, testUserId = {}, page = {}, size = {}", testUserId, 0, 10);
        Page<ServerStatusChangeHistoryEntity> actual = serverStatusChangeHistoryRepository.findAllByServer_UserInfo_Username(testUserId, PageRequest.of(0, 10));

        assertThat(actual.isEmpty()).isEqualTo(false);
        assertThat(actual.getTotalPages()).isEqualTo(1);
        assertThat(actual.getTotalElements()).isEqualTo(1L);
        assertThat(actual.getContent().get(0)).isEqualTo(serverStatusChangeHistory);

        log.info("findAllByServer_UserInfo_Username()2, testUserId = {}, page = {}, size = {}", testUserId, 1, 10);
        actual = serverStatusChangeHistoryRepository.findAllByServer_UserInfo_Username(testUserId, PageRequest.of(1, 10));
        assertThat(actual.isEmpty()).isEqualTo(true);
        log.info("test_findAllByServer_UserInfo_Username: Finished");
    }
}