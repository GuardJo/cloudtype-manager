package org.github.guardjo.cloudtype.manager.util;

import org.github.guardjo.cloudtype.manager.model.domain.*;

public class TestDataGenerator {
    public static UserInfoEntity userInfoEntity(String username) {
        return UserInfoEntity.builder()
                .username(username)
                .name("Tester")
                .password("1234")
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

    public static ServerInfoEntity serverInfoEntity(Long serverId, String serverName, String healthCheckUrl, boolean activate, UserInfoEntity userInfo) {
        return ServerInfoEntity.builder()
                .id(serverId)
                .serverName(serverName)
                .healthCheckUrl(healthCheckUrl)
                .hostingUrl(healthCheckUrl)
                .managementUrl(healthCheckUrl)
                .activate(activate)
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

    public static AppPushTokenEntity appPushTokenEntity(Long id, String token, UserInfoEntity userInfo) {
        return AppPushTokenEntity
                .builder()
                .id(id)
                .token(token)
                .device("WEB")
                .userInfo(userInfo)
                .build();
    }

    public static AppPushTokenEntity appPushTokenEntity(String token, String deviceId, UserInfoEntity userInfo) {
        return AppPushTokenEntity
                .builder()
                .token(token)
                .device(deviceId)
                .userInfo(userInfo)
                .build();
    }

    public static ServerStatusChangeHistoryEntity serverStatusChangeHistoryEntity(ServerInfoEntity serverInfo) {
        return ServerStatusChangeHistoryEntity.builder()
                .server(serverInfo)
                .statusCode(400)
                .errorCategory("BAD_REQUEST")
                .build();
    }
}
