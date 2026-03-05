package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {
    /**
     * 주어진 token에 대한 refresh_token Entity를 조회한다.
     *
     * @param token refresh_token 값
     * @return refresh_token Entity
     */
    Optional<RefreshTokenEntity> findByToken(String token);

    /**
     * 주어진 시간값을 기준으로 마지막 수정일자가 이전인 요소들을 삭제한다.
     *
     * @param expiredAt 만료 기한
     * @return 삭제된 row 수
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RefreshTokenEntity r where r.modifiedAt < :expiredAt")
    int deleteAllByModifiedAtBefore(@Param("expiredAt") LocalDateTime expiredAt);

    /**
     * 주어진 토큰 값에 해당하는 refresh_token 데이터들을 삭제한다.
     *
     * @param token refresh_token
     * @return 삭제된 row 수
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RefreshTokenEntity r where r.token = :token")
    int deleteAllByToken(@Param("token") String token);
}
