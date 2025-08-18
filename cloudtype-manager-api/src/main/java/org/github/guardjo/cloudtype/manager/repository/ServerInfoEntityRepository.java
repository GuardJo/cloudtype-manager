package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.repository.querydsl.ServerInfoQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServerInfoEntityRepository extends JpaRepository<ServerInfoEntity, Long>, ServerInfoQueryRepository {
    /**
     * 특정 계정 식별키와 연관된 server_info Entity 목록을 반환한다.
     *
     * @param userInfoUsername user_info 식별키
     * @return server_info 목록
     */
    List<ServerInfoEntity> findAllByUserInfo_Username(String userInfoUsername);

    /**
     * 특정 계정 식별키와 서버 식별키에 해당하는 server_info Entity를 반환한다.
     *
     * @param id               서버 식별키
     * @param userInfoUsername 계정 식별키
     * @return (Optional) server_info Entity
     */
    Optional<ServerInfoEntity> findByIdAndUserInfo_Username(Long id, String userInfoUsername);
}
