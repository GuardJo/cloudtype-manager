package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {
    /**
     * 주어진 계정 식별키에 해당하는 refresh_token Entity를 조회한다.
     *
     * @param userInfoUsername user_info 식별키
     * @return refresh_token Entity
     */
    Optional<RefreshTokenEntity> findByUserInfo_Username(String userInfoUsername);
}
