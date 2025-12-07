package org.github.guardjo.cloudtype.manager.model.vo;

/* 비활성 서버 알림 발송 정보 */
public record InactiveServerNotification(
        Long serverId,
        String serverName,
        String userId,
        String userName,
        String appPushToken
) {
}
