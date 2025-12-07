package org.github.guardjo.cloudtype.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.vo.FirebaseMessageRequest;
import org.github.guardjo.cloudtype.manager.model.vo.InactiveServerNotification;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.util.FirebaseMessageSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

        if (Objects.isNull(inactiveServerIds) || inactiveServerIds.isEmpty()) {
            log.warn("Not founds InactiveServerIds");
            return CompletableFuture.completedFuture(sendCount);
        }

        List<InactiveServerNotification> inactiveServerNotifications = serverInfoRepository.findAllInactiveServerNotifications(inactiveServerIds);

        List<FirebaseMessageRequest> firebaseMessageRequests = inactiveServerNotifications.stream()
                .map(noti -> {
                    return new FirebaseMessageRequest(
                            noti.appPushToken(),
                            INACTIVE_MESSAGE_TITLE,
                            String.format(INACTIVE_MESSAGE_FORMAT, noti.serverName())
                    );
                })
                .toList();

        messageSender.sendMessage(firebaseMessageRequests);
        sendCount = firebaseMessageRequests.size();

        return CompletableFuture.completedFuture(sendCount);
    }
}
