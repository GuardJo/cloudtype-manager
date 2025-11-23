package org.github.guardjo.cloudtype.manager.util;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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

            ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
            given(firebaseMessaging.send(messageArgumentCaptor.capture())).willReturn("test-response");

            assertThatCode(() -> firebaseMessageSender.sendMessage(targetToken, title, body))
                    .doesNotThrowAnyException();

            Message actual = messageArgumentCaptor.getValue();
            Field tokenField = ReflectionUtils.makeAccessible(
                    Message.class.getDeclaredField("token")
            );
            Field notificationField = ReflectionUtils.makeAccessible(
                    Message.class.getDeclaredField("notification")
            );
            Field titleField = ReflectionUtils.makeAccessible(
                    Notification.class.getDeclaredField("title")
            );
            Field bodyField = ReflectionUtils.makeAccessible(
                    Notification.class.getDeclaredField("body")
            );

            assertThat(actual).isNotNull();
            assertThat(tokenField.get(actual)).isEqualTo(targetToken);
            Notification notification = (Notification) notificationField.get(actual);
            assertThat(titleField.get(notification)).isEqualTo(title);
            assertThat(bodyField.get(notification)).isEqualTo(body);

            then(firebaseMessaging).should().send(any(Message.class));
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

            assertThatCode(() -> firebaseMessageSender.sendMessage(targetToken, title, body))
                    .doesNotThrowAnyException();
        }
    }
}