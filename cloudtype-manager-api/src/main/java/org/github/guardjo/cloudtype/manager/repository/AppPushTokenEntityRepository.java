package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.AppPushTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppPushTokenEntityRepository extends JpaRepository<AppPushTokenEntity, Long> {
    /**
     * 주어진 app push token 값에 해당하는 app_push_token Entity를 조회한다.
     *
     * @param token AppPush token
     * @return 동일한 토큰값을 지닌 AppPushToken Entity
     */
    Optional<AppPushTokenEntity> findByToken(String token);
}
