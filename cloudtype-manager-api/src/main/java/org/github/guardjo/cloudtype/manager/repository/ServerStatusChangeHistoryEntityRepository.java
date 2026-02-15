package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.ServerStatusChangeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerStatusChangeHistoryEntityRepository extends JpaRepository<ServerStatusChangeHistoryEntity, Long> {
}
