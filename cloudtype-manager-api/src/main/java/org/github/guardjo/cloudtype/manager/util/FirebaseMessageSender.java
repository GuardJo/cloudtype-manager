package org.github.guardjo.cloudtype.manager.util;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.vo.FirebaseMessageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessageSender {
    private final FirebaseMessaging firebaseMessaging;

    /**
     * Firebase를 통해 푸시 메시지를 벌크로 전송
     *
     * @param firebaseMessageRequests 벌크로 전송할 메시지 정보 (token, title, body)
     */
    public void sendMessage(List<FirebaseMessageRequest> firebaseMessageRequests) {
        List<Message> messages = firebaseMessageRequests.stream()
                .map(FirebaseMessageRequest::toMessage)
                .toList();

        BatchResponse result = null;
        try {
            result = firebaseMessaging.sendEach(messages);

            log.info("FirebaseMessage send result, successCount = {}, failureCount = {}", result.getSuccessCount(), result.getFailureCount());
        } catch (FirebaseMessagingException e) {
            log.warn("Failed send firebase message, cause = {}", e.getMessage(), e);
        }
    }
}
