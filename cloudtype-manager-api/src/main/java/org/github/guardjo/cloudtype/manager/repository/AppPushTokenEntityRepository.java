package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.AppPushTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPushTokenEntityRepository extends JpaRepository<AppPushTokenEntity, Long> {
}
