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

        List<?> result = entityManager.createNativeQuery(
                        "select je.job_execution_id " +
                                "from batch_job_execution je " +
                                "where je.status = 'COMPLETED' " +
                                "  and je.end_time < :targetDate " +
                                "order by je.end_time " +
                                "limit :limit;"
                        , Long.class
                )
                .setParameter("targetDate", dateTime)
                .setParameter("limit", limit)
                .getResultList();

        return result.stream()
                .map(v -> ((Number) v).longValue())
                .toList();
    }

    @Override
    public long deleteAllJobExecutionContextInJobExecutionIds(List<Long> jobExecutionIds) {
        if (jobExecutionIds == null || jobExecutionIds.isEmpty()) {
            return 0;
        }

        return entityManager.createNativeQuery(
                        "delete from batch_job_execution_context " +
                                "where job_execution_id in (:jobExecutionIds)"
                )
                .setParameter("jobExecutionIds", jobExecutionIds)
                .executeUpdate();
    }

    @Override
    public long deleteAllJobExecutionParamsInJobExecutionIds(List<Long> jobExecutionIds) {
        if (jobExecutionIds == null || jobExecutionIds.isEmpty()) {
            return 0;
        }

        return entityManager.createNativeQuery(
                        "delete from batch_job_execution_params " +
                                "where job_execution_id in (:jobExecutionIds)"
                )
                .setParameter("jobExecutionIds", jobExecutionIds)
                .executeUpdate();
    }

    @Override
    public long deleteAllStepExecutionInJobExecutionIds(List<Long> jobExecutionIds) {
        if (jobExecutionIds == null || jobExecutionIds.isEmpty()) {
            return 0;
        }

        return entityManager.createNativeQuery(
                        "delete from batch_step_execution " +
                                "where job_execution_id in (:jobExecutionIds)"
                )
                .setParameter("jobExecutionIds", jobExecutionIds)
                .executeUpdate();
    }

    @Override
    public long deleteAllStepExecutionContextInJobExecutionIds(List<Long> jobExecutionIds) {
        if (jobExecutionIds == null || jobExecutionIds.isEmpty()) {
            return 0;
        }

        return entityManager.createNativeQuery(
                        "delete from batch_step_execution_context " +
                                "where step_execution_id in (" +
                                "    select step_execution_id " +
                                "    from batch_step_execution " +
                                "    where job_execution_id in (:jobExecutionIds)" +
                                ")"
                )
                .setParameter("jobExecutionIds", jobExecutionIds)
                .executeUpdate();
    }

    @Override
    public long deleteAllJobExecutionInJobExecutionIds(List<Long> jobExecutionIds) {
        if (jobExecutionIds == null || jobExecutionIds.isEmpty()) {
            return 0;
        }

        return entityManager.createNativeQuery(
                        "delete from batch_job_execution " +
                                "where job_execution_id in (:jobExecutionIds)"
                )
                .setParameter("jobExecutionIds", jobExecutionIds)
                .executeUpdate();
    }

    @Override
    public long deleteAllJobInstanceInJobExecutionIds(List<Long> jobExecutionIds) {
        List<Long> jobInstanceIds = selectAllJobInstanceIdsInJobExecutionIds(jobExecutionIds);
        return deleteAllJobInstanceInJobInstanceIds(jobInstanceIds);
    }

    @Override
    public List<Long> selectAllJobInstanceIdsInJobExecutionIds(List<Long> jobExecutionIds) {
        if (jobExecutionIds == null || jobExecutionIds.isEmpty()) {
            return List.of();
        }

        List<?> jobInstanceIdResultRows = entityManager.createNativeQuery(
                        "select distinct je.job_instance_id " +
                                "from batch_job_execution je " +
                                "where je.job_execution_id in (:jobExecutionIds)",
                        Long.class
                )
                .setParameter("jobExecutionIds", jobExecutionIds)
                .getResultList();

        return jobInstanceIdResultRows.stream()
                .map(v -> ((Number) v).longValue())
                .toList();
    }

    @Override
    public long deleteAllJobInstanceInJobInstanceIds(List<Long> jobInstanceIds) {
        if (jobInstanceIds == null || jobInstanceIds.isEmpty()) {
            return 0;
        }

        return entityManager.createNativeQuery(
                        "delete from batch_job_instance ji " +
                                "where ji.job_instance_id in (:jobInstanceIds) " +
                                "  and not exists (" +
                                "      select 1 " +
                                "      from batch_job_execution je " +
                                "      where je.job_instance_id = ji.job_instance_id" +
                                "  )"
                )
                .setParameter("jobInstanceIds", jobInstanceIds)
                .executeUpdate();
    }
}
