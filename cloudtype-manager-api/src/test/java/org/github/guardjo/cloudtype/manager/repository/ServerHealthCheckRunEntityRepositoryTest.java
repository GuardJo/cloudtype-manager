package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.ServerHealthCheckRunEntity;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ServerHealthCheckRunEntityRepositoryTest {
    private final UserInfoEntity testUserInfo = TestDataGenerator.userInfoEntity("tester");
    private final List<ServerHealthCheckRunEntity> serverhealthCheckRunList = new ArrayList<>();

    @Autowired
    private UserInfoEntityRepository userInfoRepository;

    @Autowired
    private ServerInfoEntityRepository serverInfoRepository;

    @Autowired
    private ServerHealthCheckRunEntityRepository serverHealthCheckRunRepository;

    @BeforeEach
    void setUp() {
        userInfoRepository.save(testUserInfo);
        ServerInfoEntity testServer = initServerInfo(testUserInfo, "Test Server");
        ServerHealthCheckRunEntity serverHealthCheckRunEntity = serverHealthCheckRunRepository.save(ServerHealthCheckRunEntity.builder()
                .server(testServer)
                .statusCode(400)
                .errorCategory("BAD_REQUEST")
                .build());
        serverhealthCheckRunList.add(serverHealthCheckRunEntity);
    }

    @AfterEach
    void tearDown() {
        serverHealthCheckRunRepository.deleteAll();
        serverhealthCheckRunList.clear();
        serverInfoRepository.deleteAll();
        userInfoRepository.deleteAll();
    }

    ServerInfoEntity initServerInfo(UserInfoEntity userInfo, String serverName) {
        ServerInfoEntity testServer = TestDataGenerator.serverInfoEntity("Test Server", testUserInfo);
        serverInfoRepository.save(testServer);

        return testServer;
    }

    @DisplayName("특정 ServerHealthCheckRunEntity 조회")
    @Test
    void test_findById() {
        ServerHealthCheckRunEntity expected = serverhealthCheckRunList.get(0);
        Long id = expected.getId();

        ServerHealthCheckRunEntity actual = serverHealthCheckRunRepository.findById(id).orElseThrow();

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }
}