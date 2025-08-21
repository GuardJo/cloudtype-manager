package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {
    /**
     * 주어진 token에 대한 refresh_token Entity를 조회한다.
     *
     * @param token refresh_token 값
     * @return refresh_token Entity
     */
    Optional<RefreshTokenEntity> findByToken(String token);
}
