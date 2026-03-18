package org.github.guardjo.cloudtype.manager.service;

import jakarta.persistence.EntityNotFoundException;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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
        given(appPushTokenEntityRepository.findByToken(eq(token))).willReturn(Optional.empty());
        given(appPushTokenEntityRepository.save(appPushTokenEntityArgumentCaptor.capture())).willReturn(mock(AppPushTokenEntity.class));

        assertThatCode(() -> appPushService.saveAppPushToken(request, userInfo))
                .doesNotThrowAnyException();

        AppPushTokenEntity actual = appPushTokenEntityArgumentCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getToken()).isEqualTo(token);
        assertThat(actual.getDevice()).isEqualTo(device);
        assertThat(actual.getUserInfo()).isEqualTo(TESTE_USER_ENTITY);

        then(userInfoEntityRepository).should().getReferenceById(eq(userInfo.id()));
        then(appPushTokenEntityRepository).should().findByToken(eq(token));
        then(appPushTokenEntityRepository).should().save(any(AppPushTokenEntity.class));
    }

    @DisplayName("가존 AppPushToken 정보 갱신")
    @Test
    void test_saveAppPushToken_update_old_token() {
        String device = "WEB";
        String token = "test-app-push-token";

        AppPushTokenRequest request = new AppPushTokenRequest(device, token);
        UserInfo userInfo = UserInfo.from(TESTE_USER_ENTITY);
        AppPushTokenEntity oldAppPushTokenEntity = AppPushTokenEntity.builder()
                .id(1L)
                .token(token)
                .device("OLD")
                .userInfo(TestDataGenerator.userInfoEntity("tester2"))
                .build();

        given(userInfoEntityRepository.getReferenceById(eq(userInfo.id()))).willReturn(TESTE_USER_ENTITY);
        given(appPushTokenEntityRepository.findByToken(eq(token))).willReturn(Optional.of(oldAppPushTokenEntity));

        assertThatCode(() -> appPushService.saveAppPushToken(request, userInfo))
                .doesNotThrowAnyException();

        then(userInfoEntityRepository).should().getReferenceById(eq(userInfo.id()));
        then(appPushTokenEntityRepository).should().findByToken(eq(token));
    }

    @DisplayName("회원 및 디바이스 별 앱푸시 토큰 조회")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void test_getAppPushToken(boolean hasData) {
        String pushToken = "test-token";
        String deviceId = "test-device";
        String userId = TESTE_USER_ENTITY.getUsername();

        AppPushTokenEntity expected = TestDataGenerator.appPushTokenEntity(pushToken, deviceId, TESTE_USER_ENTITY);

        if (hasData) {
            given(appPushTokenEntityRepository.findByDeviceAndUserInfo_Username(eq(deviceId), eq(userId))).willReturn(Optional.of(expected));
            String actual = appPushService.getAppPushToken(userId, deviceId);
            assertThat(actual).isEqualTo(pushToken);
        } else {
            given(appPushTokenEntityRepository.findByDeviceAndUserInfo_Username(eq(deviceId), eq(userId))).willReturn(Optional.empty());
            assertThatCode(() -> appPushService.getAppPushToken(userId, deviceId))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        then(appPushTokenEntityRepository).should().findByDeviceAndUserInfo_Username(eq(deviceId), eq(userId));
    }

    @DisplayName("특정회원의 디바이스에 해당하는 앱푸시토큰 갱신")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void test_updateAppPushToken(boolean hasData) {
        String pushToken = "test-token";
        String deviceId = "test-device";
        String userId = TESTE_USER_ENTITY.getUsername();
        AppPushTokenRequest request = new AppPushTokenRequest(deviceId, "update-token");

        if (hasData) {
            AppPushTokenEntity oldToken = spy(TestDataGenerator.appPushTokenEntity(pushToken, deviceId, TESTE_USER_ENTITY));
            given(appPushTokenEntityRepository.findByDeviceAndUserInfo_Username(eq(deviceId), eq(userId))).willReturn(Optional.of(oldToken));
            given(appPushTokenEntityRepository.findByToken(eq(request.token()))).willReturn(Optional.empty());
            assertThatCode(() -> appPushService.updateAppPushToken(userId, request))
                    .doesNotThrowAnyException();

            then(oldToken).should().setToken(eq(request.token()));
        } else {
            given(appPushTokenEntityRepository.findByDeviceAndUserInfo_Username(eq(deviceId), eq(userId))).willReturn(Optional.empty());
            assertThatCode(() -> appPushService.updateAppPushToken(userId, request))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        then(appPushTokenEntityRepository).should().findByDeviceAndUserInfo_Username(eq(deviceId), eq(userId));
        then(appPushTokenEntityRepository).should(atLeast(0)).findByToken(eq(request.token()));
    }

    @DisplayName("앱푸시토큰 갱신 시 이미 존재하는 토큰으로 갱신할 경우")
    @Test
    void test_updateAppPushToken_duplicate_token() {
        String pushToken = "test-token";
        String deviceId = "test-device";
        String userId = TESTE_USER_ENTITY.getUsername();
        AppPushTokenRequest request = new AppPushTokenRequest(deviceId, "update-token");

        AppPushTokenEntity oldToken = TestDataGenerator.appPushTokenEntity(1L, pushToken, deviceId, TESTE_USER_ENTITY);
        AppPushTokenEntity duplicateToken = TestDataGenerator.appPushTokenEntity(2L, request.token(), TESTE_USER_ENTITY);
        given(appPushTokenEntityRepository.findByDeviceAndUserInfo_Username(eq(deviceId), eq(userId))).willReturn(Optional.of(oldToken));
        given(appPushTokenEntityRepository.findByToken(eq(request.token()))).willReturn(Optional.of(duplicateToken));

        assertThatCode(() -> appPushService.updateAppPushToken(userId, request))
                .isInstanceOf(DuplicateKeyException.class);

        then(appPushTokenEntityRepository).should().findByDeviceAndUserInfo_Username(eq(deviceId), eq(userId));
        then(appPushTokenEntityRepository).should().findByToken(eq(request.token()));
    }
}