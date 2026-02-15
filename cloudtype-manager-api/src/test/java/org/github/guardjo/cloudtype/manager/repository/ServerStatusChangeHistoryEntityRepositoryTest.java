package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.ServerStatusChangeHistoryEntity;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ServerStatusChangeHistoryEntityRepositoryTest {
    private final UserInfoEntity testUserInfo = TestDataGenerator.userInfoEntity("tester");
    private final List<ServerStatusChangeHistoryEntity> serverhealthCheckRunList = new ArrayList<>();

    @Autowired
    private UserInfoEntityRepository userInfoRepository;

    @Autowired
    private ServerInfoEntityRepository serverInfoRepository;

    @Autowired
    private ServerStatusChangeHistoryEntityRepository serverHealthCheckRunRepository;

    @BeforeEach
    void setUp() {
        userInfoRepository.save(testUserInfo);
        ServerInfoEntity testServer = initServerInfo(testUserInfo, "Test Server");
        ServerStatusChangeHistoryEntity serverStatusChangeHistoryEntity = serverHealthCheckRunRepository.save(ServerStatusChangeHistoryEntity.builder()
                .server(testServer)
                .statusCode(400)
                .errorCategory("BAD_REQUEST")
                .build());
        serverhealthCheckRunList.add(serverStatusChangeHistoryEntity);
    }

    @AfterEach
    void tearDown() {
        serverHealthCheckRunRepository.deleteAll();
        serverhealthCheckRunList.clear();
        serverInfoRepository.deleteAll();
        userInfoRepository.deleteAll();
    }

    ServerInfoEntity initServerInfo(UserInfoEntity userInfo, String serverName) {
        ServerInfoEntity testServer = TestDataGenerator.serverInfoEntity(serverName, userInfo);
        serverInfoRepository.save(testServer);

        return testServer;
    }

    @DisplayName("특정 ServerHealthCheckRunEntity 조회")
    @Test
    void test_findById() {
        ServerStatusChangeHistoryEntity expected = serverhealthCheckRunList.get(0);
        Long id = expected.getId();

        ServerStatusChangeHistoryEntity actual = serverHealthCheckRunRepository.findById(id).orElseThrow();

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }
}