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

        AppPushTokenEntity newAppPushToken = AppPushTokenEntity.builder()
                .token(tokenRequest.token())
                .device(tokenRequest.device())
                .userInfo(userInfoEntity)
                .build();

        newAppPushToken = appPushTokenRepository.save(newAppPushToken);

        log.info("Save new AppPushToken Entity, id = {}", newAppPushToken.getId());
    }
}
