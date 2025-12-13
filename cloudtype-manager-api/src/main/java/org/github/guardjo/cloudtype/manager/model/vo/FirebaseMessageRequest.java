package org.github.guardjo.cloudtype.manager.model.vo;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

/* Firebase 푸시 알림 메시지에 담을 데이터 정보 */
public record FirebaseMessageRequest(
        Long targetTokenId,
        String targetToken,
        String title,
        String body
) {
    public Message toMessage() {
        return Message.builder()
                .setToken(this.targetToken)
                .setNotification(Notification.builder()
                        .setTitle(this.title)
                        .setBody(this.body)
                        .build())
                .build();
    }
}
