package org.github.guardjo.cloudtype.manager.util;

import org.github.guardjo.cloudtype.manager.model.domain.AppPushTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.RefreshTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;

import java.util.List;

public class TestDataGenerator {
    public static UserInfoEntity userInfoEntity(String username) {
        return UserInfoEntity.builder()
                .username(username)
                .name("Tester")
                .password("1234")
                .build();
    }

    public static UserInfoEntity userInfoEntity(UserInfoEntity userInfo, List<AppPushTokenEntity> appPushTokenEntityList) {
        return UserInfoEntity.builder()
                .username(userInfo.getUsername())
                .name(userInfo.getName())
                .password(userInfo.getPassword())
                .appPushTokens(appPushTokenEntityList)
                .build();
    }

    public static ServerInfoEntity serverInfoEntity(String serverName, UserInfoEntity userInfo) {
        return ServerInfoEntity.builder()
                .serverName(serverName)
                .healthCheckUrl("https://google.com")
                .hostingUrl("https://github.com")
                .managementUrl("https://cloudtype.io")
                .userInfo(userInfo)
                .build();
    }

    public static ServerInfoEntity serverInfoEntity(Long serverId, String serverName, UserInfoEntity userInfo) {
        return ServerInfoEntity.builder()
                .id(serverId)
                .serverName(serverName)
                .healthCheckUrl("https://google.com")
                .hostingUrl("https://github.com")
                .managementUrl("https://cloudtype.io")
                .userInfo(userInfo)
                .build();
    }

    public static RefreshTokenEntity refreshTokenEntity(String refreshToken, UserInfoEntity userInfo) {
        return RefreshTokenEntity.builder()
                .token(refreshToken)
                .userInfo(userInfo)
                .build();
    }

    public static AppPushTokenEntity appPushTokenEntity(String token, UserInfoEntity userInfo) {
        return AppPushTokenEntity
                .builder()
                .token(token)
                .device("WEB")
                .userInfo(userInfo)
                .build();
    }
}
