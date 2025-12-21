package org.github.guardjo.cloudtype.manager.model.vo;

import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;

import java.time.LocalDateTime;

public record UserInfo(
        String id,
        String name,
        String email,
        LocalDateTime createAt,
        LocalDateTime modifiedAt
) {

    public static UserInfo from(UserInfoEntity entity) {
        return new UserInfo(
                entity.getUsername(),
                entity.getName(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
