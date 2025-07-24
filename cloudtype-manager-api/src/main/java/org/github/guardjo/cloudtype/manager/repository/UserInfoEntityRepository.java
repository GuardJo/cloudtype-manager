package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoEntityRepository extends JpaRepository<UserInfoEntity, String> {
}
