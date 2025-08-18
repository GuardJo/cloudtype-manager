package org.github.guardjo.cloudtype.manager.repository.querydsl;

import com.querydsl.core.types.dsl.CaseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.domain.QServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class ServerInfoQueryRepositoryImpl extends QuerydslRepositorySupport implements ServerInfoQueryRepository {

    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     */
    public ServerInfoQueryRepositoryImpl() {
        super(ServerInfoEntity.class);
    }

    @Override
    public long updateActivateStatus(List<Long> idsToActivate, List<Long> idsToDeactivate) {
        if (idsToActivate.isEmpty() && idsToDeactivate.isEmpty()) {
            log.debug("Not founds update server_info");

            return 0L;
        }

        QServerInfoEntity qServerInfoEntity = QServerInfoEntity.serverInfoEntity;

        return update(qServerInfoEntity)
                .set(qServerInfoEntity.activate,
                        new CaseBuilder()
                                .when(qServerInfoEntity.id.in(idsToActivate))
                                .then(true)
                                .when(qServerInfoEntity.id.in(idsToDeactivate))
                                .then(false)
                                .otherwise(qServerInfoEntity.activate))
                .where(qServerInfoEntity.id.in(idsToActivate)
                        .or(qServerInfoEntity.id.in(idsToDeactivate)))
                .execute();
    }
}
