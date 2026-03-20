package org.github.guardjo.cloudtype.manager.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.github.guardjo.cloudtype.manager.model.domain.*;
import org.github.guardjo.cloudtype.manager.model.request.CustomerInquiryRequest;
import org.github.guardjo.cloudtype.manager.model.vo.FirebaseMessageRequest;
import org.github.guardjo.cloudtype.manager.model.vo.InactiveServerNotification;
import org.github.guardjo.cloudtype.manager.repository.*;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {
    private final static UserInfoEntity TEST_USER_ENTITY = TestDataGenerator.userInfoEntity("Tester");
    private final static AppPushTokenEntity TEST_APP_PUSH_TOKEN = TestDataGenerator.appPushTokenEntity(1L, "test-token", TEST_USER_ENTITY);

    @Mock
    private FirebaseMessageSender messageSender;

    @Mock
    private ServerInfoEntityRepository serverInfoRepository;

    @Mock
    private AppPushTokenEntityRepository appPushTokenRepository;

    @Mock
    private AppPushMsgEntityRepository appPushMsgRepository;

    @Mock
    private UserInfoEntityRepository userInfoRepository;

    @Mock
    private CustomerInquiryEntityRepository customerInquiryRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @DisplayName("서버 비활성화 알림 테스트")
    @Test
    void test_sendServerInactiveNotification() throws FirebaseMessagingException, ExecutionException, InterruptedException {
        List<ServerInfoEntity> serverInfoEntities = List.of(TestDataGenerator.serverInfoEntity(1L, "server1", TEST_USER_ENTITY));
        List<Long> serverInfoIds = serverInfoEntities.stream()
                .mapToLong(ServerInfoEntity::getId)
                .boxed()
                .toList();
        List<InactiveServerNotification> inactiveServerNotifications = serverInfoEntities.stream()
                .map(serverInfoEntity -> {
                    UserInfoEntity userInfo = serverInfoEntity.getUserInfo();
                    List<InactiveServerNotification> inactiveInfo = new ArrayList<>();
                    for (AppPushTokenEntity appPushToken : List.of(TEST_APP_PUSH_TOKEN)) {
                        inactiveInfo.add(new InactiveServerNotification(serverInfoEntity.getId(), serverInfoEntity.getServerName(), userInfo.getUsername(), userInfo.getName(), appPushToken.getId(), appPushToken.getToken()));
                    }

                    return inactiveInfo;
                })
                .flatMap(List::stream)
                .toList();
        ArgumentCaptor<List<FirebaseMessageRequest>> messageRequestsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<AppPushMsgEntity>> pushMsgsCaptor = ArgumentCaptor.forClass(List.class);

        long expected = serverInfoIds.size();

        given(serverInfoRepository.findAllInactiveServerNotifications(eq(serverInfoIds))).willReturn(inactiveServerNotifications);
        willDoNothing().given(messageSender).sendMessage(messageRequestsCaptor.capture());
        given(appPushTokenRepository.getReferenceById(eq(TEST_APP_PUSH_TOKEN.getId()))).willReturn(TEST_APP_PUSH_TOKEN);
        willReturn(mock(List.class)).given(appPushMsgRepository).saveAll(pushMsgsCaptor.capture());

        long actual = notificationService.sendServerInactiveNotification(serverInfoIds).get();
        List<FirebaseMessageRequest> messageRequests = messageRequestsCaptor.getValue();
        List<AppPushMsgEntity> appPushMsgEntities = pushMsgsCaptor.getValue();

        assertThat(actual).isEqualTo(expected);
        assertThat(messageRequests.size()).isEqualTo(inactiveServerNotifications.size());

        for (int i = 0; i < messageRequests.size(); i++) {
            FirebaseMessageRequest messageRequest = messageRequests.get(i);
            InactiveServerNotification inactiveServerNotification = inactiveServerNotifications.get(i);
            AppPushMsgEntity appPushMsgEntity = appPushMsgEntities.get(i);

            assertThat(messageRequest.targetToken()).isEqualTo(inactiveServerNotification.appPushToken());
            assertThat(messageRequest.body().contains(inactiveServerNotification.serverName())).isTrue();
            assertThat(appPushMsgEntity).isNotNull();

            assertThat(appPushMsgEntity.getTitle()).isEqualTo(messageRequest.title());
            assertThat(appPushMsgEntity.getBody()).isEqualTo(messageRequest.body());
            assertThat(appPushMsgEntity.getAppPushToken().getId()).isEqualTo(messageRequest.targetTokenId());
            assertThat(appPushMsgEntity.getAppPushToken().getToken()).isEqualTo(messageRequest.targetToken());
        }

        then(serverInfoRepository).should().findAllInactiveServerNotifications(eq(serverInfoIds));
        then(messageSender).should().sendMessage(any(List.class));
        then(appPushTokenRepository).should().getReferenceById(eq(TEST_APP_PUSH_TOKEN.getId()));
        then(appPushMsgRepository).should().saveAll(any(List.class));
    }

    @DisplayName("고객 문의 저장")
    @Test
    void test_saveCustomerInquiry() {
        CustomerInquiryRequest inquiryRequest = new CustomerInquiryRequest("Test-Title", "Test-type", "Test-content");

        ArgumentCaptor<CustomerInquiryEntity> inquiryEntityCaptor = ArgumentCaptor.forClass(CustomerInquiryEntity.class);

        given(userInfoRepository.getReferenceById(eq(TEST_USER_ENTITY.getUsername()))).willReturn(TEST_USER_ENTITY);
        given(customerInquiryRepository.save(inquiryEntityCaptor.capture())).willReturn(mock(CustomerInquiryEntity.class));

        assertThatCode(() -> notificationService.saveCustomerInquiry(TEST_USER_ENTITY.getUsername(), inquiryRequest))
                .doesNotThrowAnyException();

        CustomerInquiryEntity actual = inquiryEntityCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(inquiryRequest.title());
        assertThat(actual.getInquiryType()).isEqualTo(inquiryRequest.inquiryType());
        assertThat(actual.getContent()).isEqualTo(inquiryRequest.content());
        assertThat(actual.getUserInfo()).isEqualTo(TEST_USER_ENTITY);

        then(userInfoRepository).should().getReferenceById(eq(TEST_USER_ENTITY.getUsername()));
        then(customerInquiryRepository).should().save(any(CustomerInquiryEntity.class));
    }
}