package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerInfoEntityRepository extends JpaRepository<ServerInfoEntity, Long> {
    /**
     * 특정 계정 식별키와 연관된 server_info Entity 목록을 반환한다.
     *
     * @param userInfoUsername user_info 식별키
     * @return server_info 목록
     */
    List<ServerInfoEntity> findAllByUserInfo_Username(String userInfoUsername);
}
