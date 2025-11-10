package org.github.guardjo.cloudtype.manager.service;

import jakarta.persistence.EntityNotFoundException;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.request.CreateServerRequest;
import org.github.guardjo.cloudtype.manager.model.vo.ServerDetail;
import org.github.guardjo.cloudtype.manager.model.vo.ServerSummary;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ServerManagementServiceTest {
    private final static UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");

    @Mock
    private ServerInfoEntityRepository serverInfoRepository;

    @Mock
    private UserInfoEntityRepository userInfoRepository;

    @InjectMocks
    private ServerManagementServiceImpl serverManagementService;

    @DisplayName("서버 목록 조회")
    @Test
    void test_getServerSummaries() {
        String username = TEST_USER.getUsername();

        List<ServerInfoEntity> serverInfoEntities = List.of(
                TestDataGenerator.serverInfoEntity("server1", TEST_USER),
                TestDataGenerator.serverInfoEntity("server2", TEST_USER)
        );

        List<ServerSummary> expected = serverInfoEntities.stream()
                .map(ServerSummary::of)
                .collect(Collectors.toList());

        given(serverInfoRepository.findAllByUserInfo_Username(eq(username))).willReturn(serverInfoEntities);

        List<ServerSummary> actual = serverManagementService.getServerSummaries(UserInfo.from(TEST_USER));
        assertThat(actual).isEqualTo(expected);

        then(serverInfoRepository).should().findAllByUserInfo_Username(eq(username));
    }

    @DisplayName("신규 서버 등록")
    @Test
    void test_addServer() {
        UserInfo testUser = UserInfo.from(TEST_USER);
        CreateServerRequest createServerRequest = new CreateServerRequest(
                "Test Server",
                "https://google.com",
                "https:/github.com"
        );

        ArgumentCaptor<ServerInfoEntity> serverInfoEntityArgumentCaptor = ArgumentCaptor.forClass(ServerInfoEntity.class);
        given(userInfoRepository.getReferenceById(eq(testUser.id()))).willReturn(TEST_USER);
        given(serverInfoRepository.save(serverInfoEntityArgumentCaptor.capture())).willReturn(mock(ServerInfoEntity.class));

        assertThatCode(() -> serverManagementService.addServer(createServerRequest, testUser))
                .doesNotThrowAnyException();
        ServerInfoEntity actual = serverInfoEntityArgumentCaptor.getValue();
        assertThat(actual.getServerName()).isEqualTo(createServerRequest.serverName());
        assertThat(actual.getHostingUrl()).isEqualTo(createServerRequest.serverUrl());
        assertThat(actual.getHealthCheckUrl()).isEqualTo(createServerRequest.serverUrl());
        assertThat(actual.getManagementUrl()).isEqualTo(createServerRequest.managementUrl());

        then(userInfoRepository).should().getReferenceById(eq(testUser.id()));
        then(serverInfoRepository).should().save(any(ServerInfoEntity.class));
    }

    @DisplayName("서버 상세 정보 조회")
    @Test
    void test_getServerDetail() {
        String username = TEST_USER.getUsername();
        Long serverId = 1L;
        ServerInfoEntity serverInfoEntity = TestDataGenerator.serverInfoEntity(serverId, "Test Server", TEST_USER);
        ServerDetail expected = ServerDetail.from(serverInfoEntity);

        given(serverInfoRepository.findByIdAndUserInfo_Username(eq(serverId), eq(username))).willReturn(Optional.of(serverInfoEntity));

        ServerDetail actual = serverManagementService.getServerDetail(serverId, UserInfo.from(TEST_USER));
        assertThat(actual).isEqualTo(expected);

        then(serverInfoRepository).should().findByIdAndUserInfo_Username(eq(serverId), eq(username));
    }

    @DisplayName("서버 상세 정보 조회 -> 조회 실패")
    @Test
    void test_getServerDetail_not_found_exception() {
        String username = TEST_USER.getUsername();
        Long serverId = 1L;

        given(serverInfoRepository.findByIdAndUserInfo_Username(eq(serverId), eq(username))).willReturn(Optional.empty());

        assertThatCode(() -> serverManagementService.getServerDetail(serverId, UserInfo.from(TEST_USER)))
                .isInstanceOf(EntityNotFoundException.class);

        then(serverInfoRepository).should().findByIdAndUserInfo_Username(eq(serverId), eq(username));
    }

    @DisplayName("특정 서버 정보 삭제")
    @Test
    void test_deleteMyServer() {
        Long serverId = 1L;
        ServerInfoEntity serverInfo = TestDataGenerator.serverInfoEntity(serverId, "Test Server", TEST_USER);
        UserInfo testUser = UserInfo.from(TEST_USER);

        given(serverInfoRepository.findById(eq(serverId))).willReturn(Optional.of(serverInfo));
        willDoNothing().given(serverInfoRepository).deleteById(eq(serverId));

        assertThatCode(() -> serverManagementService.deleteMyServer(serverId, testUser))
                .doesNotThrowAnyException();

        then(serverInfoRepository).should().findById(eq(serverId));
        then(serverInfoRepository).should().deleteById(eq(serverId));
    }

    @DisplayName("특정 서버 정보 삭제 -> 서버 조회 실패")
    @Test
    void test_deleteMyServer_not_found_exception() {
        Long serverId = 1L;
        UserInfo testUser = UserInfo.from(TEST_USER);

        given(serverInfoRepository.findById(eq(serverId))).willReturn(Optional.empty());

        assertThatCode(() -> serverManagementService.deleteMyServer(serverId, testUser))
                .isInstanceOf(EntityNotFoundException.class);

        then(serverInfoRepository).should().findById(eq(serverId));
    }

    @DisplayName("특정 서버 정보 삭제 -> 해당 서버 삭제 권한 없음")
    @Test
    void test_deleteMyServer_not_allowed_exception() {
        Long serverId = 1L;
        ServerInfoEntity serverInfo = TestDataGenerator.serverInfoEntity(serverId, "Test Server", TEST_USER);
        UserInfo testUser = UserInfo.from(TestDataGenerator.userInfoEntity("Tester2"));

        given(serverInfoRepository.findById(eq(serverId))).willReturn(Optional.of(serverInfo));

        assertThatCode(() -> serverManagementService.deleteMyServer(serverId, testUser))
                .isInstanceOf(AccessDeniedException.class);

        then(serverInfoRepository).should().findById(eq(serverId));
    }
}