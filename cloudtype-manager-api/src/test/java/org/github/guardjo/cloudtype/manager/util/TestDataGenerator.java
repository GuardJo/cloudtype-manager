package org.github.guardjo.cloudtype.manager.util;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;

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
}
