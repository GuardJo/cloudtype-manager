package org.github.guardjo.cloudtype.manager.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.github.guardjo.cloudtype.manager.model.domain.AppPushTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.vo.InactiveServerNotification;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.util.FirebaseMessageSender;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {
    private final static UserInfoEntity TEST_USER_ENTITY = TestDataGenerator.userInfoEntity("Tester");
    private final static AppPushTokenEntity TEST_APP_PUSH_TOKEN = TestDataGenerator.appPushTokenEntity("test-token", TEST_USER_ENTITY);
    private final static UserInfoEntity TEST_USER_WITH_PUSH_TOKEN = TestDataGenerator.userInfoEntity(TEST_USER_ENTITY, List.of(TEST_APP_PUSH_TOKEN));

    @Mock
    private FirebaseMessageSender messageSender;

    @Mock
    private ServerInfoEntityRepository serverInfoRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @DisplayName("서버 비활성화 알림 테스트")
    @Test
    void test_sendServerInactiveNotification() throws FirebaseMessagingException, ExecutionException, InterruptedException {
        List<ServerInfoEntity> serverInfoEntities = List.of(TestDataGenerator.serverInfoEntity(1L, "server1", TEST_USER_WITH_PUSH_TOKEN));
        List<Long> serverInfoIds = serverInfoEntities.stream()
                .mapToLong(ServerInfoEntity::getId)
                .boxed()
                .toList();
        List<InactiveServerNotification> inactiveServerNotifications = serverInfoEntities.stream()
                .map(serverInfoEntity -> {
                    UserInfoEntity userInfo = serverInfoEntity.getUserInfo();
                    List<InactiveServerNotification> inactiveInfo = new ArrayList<>();
                    for (AppPushTokenEntity appPushToken : userInfo.getAppPushTokens()) {
                        inactiveInfo.add(new InactiveServerNotification(serverInfoEntity.getId(), serverInfoEntity.getServerName(), userInfo.getUsername(), userInfo.getName(), appPushToken.getToken()));
                    }

                    return inactiveInfo;
                })
                .flatMap(List::stream)
                .toList();

        long expected = serverInfoIds.size();


        given(serverInfoRepository.findAllInactiveServerNotifications(eq(serverInfoIds))).willReturn(inactiveServerNotifications);
        for (AppPushTokenEntity appPushToken : TEST_USER_WITH_PUSH_TOKEN.getAppPushTokens()) {
            given(messageSender.sendMessage(eq(appPushToken.getToken()), anyString(), anyString())).willReturn("Success");
        }

        long actual = notificationService.sendServerInactiveNotification(serverInfoIds).get();

        assertThat(actual).isEqualTo(expected);

        then(serverInfoRepository).should().findAllInactiveServerNotifications(eq(serverInfoIds));
        then(messageSender).should(atLeast(TEST_USER_WITH_PUSH_TOKEN.getAppPushTokens().size())).sendMessage(anyString(), anyString(), anyString());
    }
}