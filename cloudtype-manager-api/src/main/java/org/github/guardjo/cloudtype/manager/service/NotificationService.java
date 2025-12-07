package org.github.guardjo.cloudtype.manager.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {

    /**
     * 비활성화 상태의 서버들을 지닌 계정에게 해당 서버 비활성화 알림을 송신한다.
     *
     * @param inactiveServerIds 비활성화된 서버 식별키 목록
     * @return 알림 발송 건수
     */
    CompletableFuture<Long> sendServerInactiveNotification(List<Long> inactiveServerIds);
}
