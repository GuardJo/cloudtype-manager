package org.github.guardjo.cloudtype.manager.repository.querydsl;

import org.github.guardjo.cloudtype.manager.model.vo.InactiveServerNotification;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ServerInfoQueryRepository {
    /**
     * 주어진 활성화/비활성화 서버 목록에 대한 activate 상태 갱신 요청
     *
     * @param idsToActivate   활성화 서버 ID 목록
     * @param idsToDeactivate 비활성화 서버 ID 목록
     * @return 갱신된 row 수
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    long updateActivateStatus(List<Long> idsToActivate, List<Long> idsToDeactivate);

    /**
     * 주어진 비활성 서버 식별키에 대한 비활성 알림 전송 정보 목록 반환
     *
     * @param serverIds 비활성 서버 식별키
     * @return 비활성 알림 전송 정보 목록
     */
    List<InactiveServerNotification> findAllInactiveServerNotifications(List<Long> serverIds);
}
