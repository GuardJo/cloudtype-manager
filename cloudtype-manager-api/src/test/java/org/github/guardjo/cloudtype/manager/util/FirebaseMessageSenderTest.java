package org.github.guardjo.cloudtype.manager.util;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.github.guardjo.cloudtype.manager.model.vo.FirebaseMessageRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class FirebaseMessageSenderTest {

    @Nested
    @DisplayName("Mock 푸시 요청 테스트")
    @ExtendWith(MockitoExtension.class)
    class MockTest {
        @Mock
        private FirebaseMessaging firebaseMessaging;

        @InjectMocks
        private FirebaseMessageSender firebaseMessageSender;

        @DisplayName("특정 target에게 Firebase 푸시 메시지 발송")
        @Test
        void test_sendMessage() throws FirebaseMessagingException, NoSuchFieldException, IllegalAccessException {
            String targetToken = "test-token";
            String title = "Test Title";
            String body = "Test body";

            List<FirebaseMessageRequest> firebaseMessageRequests = List.of(
                    new FirebaseMessageRequest(1L, targetToken, title, body)
            );

            ArgumentCaptor<List<Message>> messageArgumentCaptor = ArgumentCaptor.forClass(List.class);
            given(firebaseMessaging.sendEach(messageArgumentCaptor.capture())).willReturn(mock(BatchResponse.class));

            assertThatCode(() -> firebaseMessageSender.sendMessage(firebaseMessageRequests))
                    .doesNotThrowAnyException();

            List<Message> actual = messageArgumentCaptor.getValue();

            assertThat(actual).isNotNull();
            assertThat(actual.size()).isEqualTo(firebaseMessageRequests.size());

            then(firebaseMessaging).should().sendEach(any(List.class));
        }
    }

    @Disabled
    @Nested
    @DisplayName("실제 푸시 요청 테스트")
    @SpringBootTest
    class RealTest {
        @Autowired
        private FirebaseMessageSender firebaseMessageSender;

        @DisplayName("특정 target에게 Firebase 푸시 메시지 발송")
        @Test
        void test_sendMessage() {
            String targetToken = "{실제 저장된 푸시토큰}";
            String title = "Test Title";
            String body = "Test body";

            List<FirebaseMessageRequest> messageRequests = List.of(new FirebaseMessageRequest(1L, targetToken, title, body));

            assertThatCode(() -> firebaseMessageSender.sendMessage(messageRequests))
                    .doesNotThrowAnyException();
        }
    }
}