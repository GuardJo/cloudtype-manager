package org.github.guardjo.cloudtype.manager.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.github.guardjo.cloudtype.manager.model.domain.AppPushTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.vo.FirebaseMessageRequest;
import org.github.guardjo.cloudtype.manager.model.vo.InactiveServerNotification;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.util.FirebaseMessageSender;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

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
        ArgumentCaptor<List<FirebaseMessageRequest>> messageRequestsCaptor = ArgumentCaptor.forClass(List.class);

        long expected = serverInfoIds.size();

        given(serverInfoRepository.findAllInactiveServerNotifications(eq(serverInfoIds))).willReturn(inactiveServerNotifications);
        willDoNothing().given(messageSender).sendMessage(messageRequestsCaptor.capture());

        long actual = notificationService.sendServerInactiveNotification(serverInfoIds).get();
        List<FirebaseMessageRequest> messageRequests = messageRequestsCaptor.getValue();

        assertThat(actual).isEqualTo(expected);
        assertThat(messageRequests.size()).isEqualTo(inactiveServerNotifications.size());

        for (int i = 0; i < messageRequests.size(); i++) {
            FirebaseMessageRequest messageRequest = messageRequests.get(i);
            InactiveServerNotification inactiveServerNotification = inactiveServerNotifications.get(i);

            assertThat(messageRequest.targetToken()).isEqualTo(inactiveServerNotification.appPushToken());
            assertThat(messageRequest.body().contains(inactiveServerNotification.serverName())).isTrue();
        }

        then(serverInfoRepository).should().findAllInactiveServerNotifications(eq(serverInfoIds));
        then(messageSender).should().sendMessage(any(List.class));
    }
}