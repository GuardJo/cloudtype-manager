package org.github.guardjo.cloudtype.manager.util;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessageSender {
    private final FirebaseMessaging firebaseMessaging;

    /**
     * Firebase 메시지 전송 요청
     *
     * @param targetToken 전송 대상 token
     * @param title       전송 메시시 제목
     * @param body        전송 메시지 본문
     * @return 메시지 전송 결과
     * @throws FirebaseMessagingException Firebase 메시지 전송 실패 예외
     */
    public String sendMessage(String targetToken, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        return firebaseMessaging.send(message);
    }
}
