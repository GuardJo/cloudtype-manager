package org.github.guardjo.cloudtype.manager.service;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.ServerStatusChangeHistoryEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.repository.ServerStatusChangeHistoryEntityRepository;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(HealthCheckService.class)
class HealthCheckServiceTest {
    @Autowired
    private MockRestServiceServer mockServer;

    @MockitoBean
    private ServerStatusChangeHistoryEntityRepository serverStatusChangeHistoryEntityRepository;

    @Autowired
    private HealthCheckService healthCheckService;

    @BeforeEach
    void setUp() {
        mockServer.reset();
    }

    @DisplayName("health-check URL 호출 테스트")
    @ParameterizedTest
    @MethodSource("getIsServerActiveParams")
    void test_isServerActive(ServerInfoEntity serverInfoEntity, ResponseCreator response, boolean expected) {
        mockServer.expect(requestTo(serverInfoEntity.getHealthCheckUrl()))
                .andRespond(response);

        CompletableFuture<Boolean> actual = healthCheckService.isServerActive(serverInfoEntity);
        assertThat(actual.join()).isEqualTo(expected);
    }

    @DisplayName("health-check URL 호출 및 상태 변경 이력 저장 테스트")
    @ParameterizedTest
    @MethodSource("getIsServerActiveParamsWithResponseStatus")
    void test_isServerActive_with_save_change_history(ServerInfoEntity serverInfoEntity, ResponseCreator response, boolean expected, int responseStatus) {
        mockServer.expect(requestTo(serverInfoEntity.getHealthCheckUrl()))
                .andRespond(response);

        ArgumentCaptor<ServerStatusChangeHistoryEntity> serverStatusHistoryArg = ArgumentCaptor.forClass(ServerStatusChangeHistoryEntity.class);
        given(serverStatusChangeHistoryEntityRepository.save(serverStatusHistoryArg.capture())).willReturn(mock(ServerStatusChangeHistoryEntity.class));

        CompletableFuture<Boolean> actual = healthCheckService.isServerActive(serverInfoEntity);
        assertThat(actual.join()).isEqualTo(expected);
        assertThat(serverStatusHistoryArg.getValue()).isNotNull();
        assertThat(serverStatusHistoryArg.getValue().getStatusCode()).isEqualTo(responseStatus);

        then(serverStatusChangeHistoryEntityRepository).should().save(any(ServerStatusChangeHistoryEntity.class));
    }

    private static Stream<Arguments> getIsServerActiveParams() {
        UserInfoEntity userInfo = TestDataGenerator.userInfoEntity("Tester");
        ServerInfoEntity serverInfo1 = TestDataGenerator.serverInfoEntity(1L, "TestServer1", "https://success.com", false, userInfo);
        ServerInfoEntity serverInfo2 = TestDataGenerator.serverInfoEntity(2L, "TestServer2", "https://failed.com", true, userInfo);

        return Stream.of(
                Arguments.of(serverInfo1, withSuccess(), true),
                Arguments.of(serverInfo2, MockRestResponseCreators.withServerError(), false)
        );
    }

    private static Stream<Arguments> getIsServerActiveParamsWithResponseStatus() {
        UserInfoEntity userInfo = TestDataGenerator.userInfoEntity("Tester");
        ServerInfoEntity serverInfo1 = TestDataGenerator.serverInfoEntity(1L, "TestServer1", "https://success.com", false, userInfo);
        ServerInfoEntity serverInfo2 = TestDataGenerator.serverInfoEntity(2L, "TestServer2", "https://failed.com", true, userInfo);

        return Stream.of(
                Arguments.of(serverInfo1, withSuccess(), true, HttpStatus.OK.value()),
                Arguments.of(serverInfo2, MockRestResponseCreators.withServerError(), false, HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }
}