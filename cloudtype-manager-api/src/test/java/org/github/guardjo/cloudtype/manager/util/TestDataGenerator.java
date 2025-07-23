package org.github.guardjo.cloudtype.manager.util;

import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;

public class TestDataGenerator {
    public static UserInfoEntity userInfoEntity(String username) {
        return UserInfoEntity.builder()
                .username(username)
                .name("Tester")
                .password("1234")
                .build();
    }
}
