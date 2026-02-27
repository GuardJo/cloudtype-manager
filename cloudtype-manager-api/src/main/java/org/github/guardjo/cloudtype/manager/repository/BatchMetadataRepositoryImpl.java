package org.github.guardjo.cloudtype.manager.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BatchMetadataRepositoryImpl implements BatchMetadataRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Long> selectAllJobExecutionIdsByStatusIsCompletedAndEndTimeBefore(LocalDateTime dateTime, int limit) {

        return entityManager.createNativeQuery(
                        "select je.job_execution_id\n" +
                                "from batch_job_execution je\n" +
                                "where je.status = 'COMPLETED'\n" +
                                "  and je.end_time < :targetDate\n" +
                                "order by je.end_time\n" +
                                "limit :limit;"
                        , Long.class
                )
                .setParameter("targetDate", dateTime)
                .setParameter("limit", limit)
                .getResultList();
    }
}
