package org.github.guardjo.cloudtype.manager.service;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;

import java.util.concurrent.CompletableFuture;

public interface HealthCheckService {
    /**
     * 주어진 URL을 통해 해당 서버가 활성화되어 있는 지 여부를 반환한다.
     *
     * @param serverInfoEntity 활성화 여부 확인 대상 서버 Entity
     * @return 활성화 여부
     */
    CompletableFuture<Boolean> isServerActive(ServerInfoEntity serverInfoEntity);
}
