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

    /**
     * 주어진 device_id 및 회원 정보에 매핑되는 app_push_token Entity를 조회한다.
     *
     * @param device   클라이언트 ID
     * @param username 회원 식별키
     * @return 해당하는 AppPushToken Entity
     */
    Optional<AppPushTokenEntity> findByDeviceAndUserInfo_Username(String device, String username);
}
