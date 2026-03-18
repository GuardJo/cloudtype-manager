package org.github.guardjo.cloudtype.manager.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.domain.AppPushTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.request.AppPushTokenRequest;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.repository.AppPushTokenEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppPushServiceImpl implements AppPushService {
    private final AppPushTokenEntityRepository appPushTokenRepository;
    private final UserInfoEntityRepository userInfoRepository;

    @Override
    @Transactional
    public void saveAppPushToken(AppPushTokenRequest tokenRequest, UserInfo userInfo) {
        log.debug("saveAppPushToken, token = {}, device = {}, userId = {}", tokenRequest.token(), tokenRequest.device(), userInfo.id());

        UserInfoEntity userInfoEntity = userInfoRepository.getReferenceById(userInfo.id());

        searchAppPushToken(tokenRequest.token())
                .ifPresentOrElse((appPushTokenEntity) -> {
                    appPushTokenEntity.setDevice(tokenRequest.device());
                    appPushTokenEntity.setUserInfo(userInfoEntity);

                    log.info("Update AppPushToken Entity, id = {}", appPushTokenEntity.getId());
                }, () -> {
                    AppPushTokenEntity newAppPushToken = AppPushTokenEntity.builder()
                            .token(tokenRequest.token())
                            .device(tokenRequest.device())
                            .userInfo(userInfoEntity)
                            .build();

                    newAppPushToken = appPushTokenRepository.save(newAppPushToken);

                    log.info("Save new AppPushToken Entity, id = {}", newAppPushToken.getId());
                });
    }

    @Override
    @Transactional(readOnly = true)
    public String getAppPushToken(String userId, String deviceId) {
        AppPushTokenEntity appPushTokenEntity = searchAppPushToken(userId, deviceId);
        return appPushTokenEntity.getToken();
    }

    @Override
    @Transactional
    public void updateAppPushToken(String username, AppPushTokenRequest tokenRequest) {
        AppPushTokenEntity appPushTokenEntity = searchAppPushToken(username, tokenRequest.device());

        searchAppPushToken(tokenRequest.token())
                .ifPresent((existedToken) -> {
                    if (!existedToken.getId().equals(appPushTokenEntity.getId())) {
                        log.warn("Already exist fcm-token, token = {}", tokenRequest.token());
                        throw new DuplicateKeyException(String.format("Already exist fcm-token, token = %s", tokenRequest.token()));
                    }
                });

        appPushTokenEntity.setToken(tokenRequest.token());

        log.debug("Updated token of AppPushToken Entity, id = {}, updateToken = {}", appPushTokenEntity.getId(), tokenRequest.token());
    }

    /*
    AppPushToken 조회
     */
    private Optional<AppPushTokenEntity> searchAppPushToken(String token) {
        return appPushTokenRepository.findByToken(token);
    }

    /*
    AppPushToken 조회
     */
    private AppPushTokenEntity searchAppPushToken(String username, String device) {
        return appPushTokenRepository.findByDeviceAndUserInfo_Username(device, username)
                .orElseThrow(() -> {
                    log.warn("Not found fcm-token, userId = {}, deviceId = {}", username, device);
                    return new EntityNotFoundException(String.format("Not found fcm-token, userId = %s, deviceId = %s", username, device));
                });
    }
}
