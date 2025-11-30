package org.github.guardjo.cloudtype.manager.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.vo.InactiveServerNotification;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.util.FirebaseMessageSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {
    private final static String INACTIVE_MESSAGE_TITLE = "서버 비활성화 알림";
    private final static String INACTIVE_MESSAGE_FORMAT = "관리중인 [%s] 서버가 비활성화 상태입니다.";

    private final FirebaseMessageSender messageSender;
    private final ServerInfoEntityRepository serverInfoRepository;

    @Async("notificationExecutor")
    @Override
    public CompletableFuture<Long> sendServerInactiveNotification(List<Long> inactiveServerIds) {
        long sendCount = 0L;

        List<InactiveServerNotification> inactiveServerNotifications = serverInfoRepository.findAllInactiveServerNotifications(inactiveServerIds);

        for (InactiveServerNotification inactiveServerNotification : inactiveServerNotifications) {
            String message = String.format(INACTIVE_MESSAGE_FORMAT, inactiveServerNotification.serverName());

            try {
                messageSender.sendMessage(inactiveServerNotification.appPushToken(), INACTIVE_MESSAGE_TITLE, message);

                sendCount++;
            } catch (FirebaseMessagingException e) {
                log.warn("Failed, send inactive-server-notification, serverId = {}, token = {}", inactiveServerNotification.serverId(), inactiveServerNotification.appPushToken(), e);
            }
        }

        return CompletableFuture.completedFuture(sendCount);
    }
}
