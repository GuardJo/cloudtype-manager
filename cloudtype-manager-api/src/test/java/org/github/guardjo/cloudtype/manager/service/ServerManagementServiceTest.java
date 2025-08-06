package org.github.guardjo.cloudtype.manager.service;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.vo.ServerSummary;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ServerManagementServiceTest {
    private final static UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");

    @Mock
    private ServerInfoEntityRepository serverInfoRepository;

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

        List<ServerSummary> actual = serverManagementService.getServerSummaries(UserInfo.of(TEST_USER));
        assertThat(actual).isEqualTo(expected);

        then(serverInfoRepository).should().findAllByUserInfo_Username(eq(username));
    }
}