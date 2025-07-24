package org.github.guardjo.cloudtype.manager.model.vo;

import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;

import java.time.LocalDateTime;

public record UserInfo(
        String id,
        String name,
        String password,
        LocalDateTime createAt,
        LocalDateTime modifiedAt
) {
    public static UserInfo from(String id, String name, String email) {
        return new UserInfo(id, name, email, null, null);
    }

    public static UserInfo of(UserInfoEntity entity) {
        return new UserInfo(
                entity.getUsername(),
                entity.getName(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
