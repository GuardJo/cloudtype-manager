package org.github.guardjo.cloudtype.manager.service;

import org.github.guardjo.cloudtype.manager.model.domain.AppPushTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.request.AppPushTokenRequest;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.repository.AppPushTokenEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AppPushServiceTest {
    private final static UserInfoEntity TESTE_USER_ENTITY = TestDataGenerator.userInfoEntity("tester");

    @Mock
    private AppPushTokenEntityRepository appPushTokenEntityRepository;

    @Mock
    private UserInfoEntityRepository userInfoEntityRepository;

    @InjectMocks
    private AppPushServiceImpl appPushService;

    @DisplayName("신규 AppPushToken 정보 저장")
    @Test
    void test_saveAppPushToken() {
        String device = "WEB";
        String token = "test-app-push-token";

        AppPushTokenRequest request = new AppPushTokenRequest(device, token);
        UserInfo userInfo = UserInfo.from(TESTE_USER_ENTITY);

        ArgumentCaptor<AppPushTokenEntity> appPushTokenEntityArgumentCaptor = ArgumentCaptor.forClass(AppPushTokenEntity.class);

        given(userInfoEntityRepository.getReferenceById(eq(userInfo.id()))).willReturn(TESTE_USER_ENTITY);
        given(appPushTokenEntityRepository.save(appPushTokenEntityArgumentCaptor.capture())).willReturn(mock(AppPushTokenEntity.class));

        assertThatCode(() -> appPushService.saveAppPushToken(request, userInfo))
                .doesNotThrowAnyException();
        AppPushTokenEntity actual = appPushTokenEntityArgumentCaptor.getValue();

        then(userInfoEntityRepository).should().getReferenceById(eq(userInfo.id()));
        then(appPushTokenEntityRepository).should().save(any(AppPushTokenEntity.class));
    }
}