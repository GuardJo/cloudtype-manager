package org.github.guardjo.cloudtype.manager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.domain.AppPushTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.request.AppPushTokenRequest;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.repository.AppPushTokenEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.springframework.stereotype.Service;

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

    /*
    AppPushToken 조회
     */
    private Optional<AppPushTokenEntity> searchAppPushToken(String token) {
        return appPushTokenRepository.findByToken(token);
    }
}
