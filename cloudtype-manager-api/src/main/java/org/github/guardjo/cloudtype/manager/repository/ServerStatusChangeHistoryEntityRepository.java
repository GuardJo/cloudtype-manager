package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.ServerStatusChangeHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerStatusChangeHistoryEntityRepository extends JpaRepository<ServerStatusChangeHistoryEntity, Long> {
    /**
     * 특정 회원이 지닌 server_info 들에 대한 server_status_change_history 목록 조회
     *
     * @param username 회원 식별키
     * @param pageable 페이지네이션
     * @return 페이지네이션 처리된 server_status_change_history 목록
     */
    Page<ServerStatusChangeHistoryEntity> findAllByServer_UserInfo_Username(String username, Pageable pageable);
}
