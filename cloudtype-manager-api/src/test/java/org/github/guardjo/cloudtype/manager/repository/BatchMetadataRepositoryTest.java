package org.github.guardjo.cloudtype.manager.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@Import(BatchMetadataRepositoryImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "classpath:org/springframework/batch/core/schema-postgresql.sql",
        "classpath:batch-metadata-insert-query.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class BatchMetadataRepositoryTest {
    @Autowired
    private BatchMetadataRepository batchMetadataRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("특정 일자 기준 이전에 해당하는 batch_job_execution 중 상태가 COMPLETED인 식별키 목록 조회")
    @Test
    void test_selectAllJobExecutionIdsByStatusIsCompletedAndEndTimeBefore() {
        LocalDateTime now = LocalDateTime.now();
        int limit = 1_000;

        List<Long> actual = batchMetadataRepository.selectAllJobExecutionIdsByStatusIsCompletedAndEndTimeBefore(now, limit);

        assertThat(actual).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.size()).isEqualTo(1);
    }

    @DisplayName("jobExecutionId 목록에 해당하는 batch_job_execution_context 삭제")
    @Test
    void test_deleteAllJobExecutionContextInJobExecutionIds() {
        List<Long> targetIds = List.of(1L);

        long actual = batchMetadataRepository.deleteAllJobExecutionContextInJobExecutionIds(targetIds);

        assertThat(actual).isEqualTo(1L);
        assertThat(count("select count(*) from batch_job_execution_context where job_execution_id = 1")).isEqualTo(0L);
        assertThat(count("select count(*) from batch_job_execution_context where job_execution_id = 2")).isEqualTo(1L);
    }

    @DisplayName("jobExecutionId 목록에 해당하는 batch_job_execution_params 삭제")
    @Test
    void test_deleteAllJobExecutionParamsInJobExecutionIds() {
        List<Long> targetIds = List.of(1L);

        long actual = batchMetadataRepository.deleteAllJobExecutionParamsInJobExecutionIds(targetIds);

        assertThat(actual).isEqualTo(1L);
        assertThat(count("select count(*) from batch_job_execution_params where job_execution_id = 1")).isEqualTo(0L);
        assertThat(count("select count(*) from batch_job_execution_params where job_execution_id = 2")).isEqualTo(1L);
    }

    @DisplayName("jobExecutionId 목록에 해당하는 batch_step_execution 삭제")
    @Test
    void test_deleteAllStepExecutionInJobExecutionIds() {
        List<Long> targetIds = List.of(1L);

        batchMetadataRepository.deleteAllStepExecutionContextInJobExecutionIds(targetIds);
        long actual = batchMetadataRepository.deleteAllStepExecutionInJobExecutionIds(targetIds);

        assertThat(actual).isEqualTo(1L);
        assertThat(count("select count(*) from batch_step_execution where job_execution_id = 1")).isEqualTo(0L);
        assertThat(count("select count(*) from batch_step_execution where job_execution_id = 2")).isEqualTo(1L);
    }

    @DisplayName("jobExecutionId 목록에 해당하는 batch_step_execution_context 삭제")
    @Test
    void test_deleteAllStepExecutionContextInJobExecutionIds() {
        List<Long> targetIds = List.of(1L);

        long actual = batchMetadataRepository.deleteAllStepExecutionContextInJobExecutionIds(targetIds);

        assertThat(actual).isEqualTo(1L);
        assertThat(count(
                "select count(*) from batch_step_execution_context sec " +
                        "join batch_step_execution se on sec.step_execution_id = se.step_execution_id " +
                        "where se.job_execution_id = 1"
        )).isEqualTo(0L);
        assertThat(count(
                "select count(*) from batch_step_execution_context sec " +
                        "join batch_step_execution se on sec.step_execution_id = se.step_execution_id " +
                        "where se.job_execution_id = 2"
        )).isEqualTo(1L);
    }

    @DisplayName("jobExecutionId 목록에 해당하는 batch_job_execution 삭제")
    @Test
    void test_deleteAllJobExecutionInJobExecutionIds() {
        List<Long> targetIds = List.of(1L);

        batchMetadataRepository.deleteAllJobExecutionContextInJobExecutionIds(targetIds);
        batchMetadataRepository.deleteAllJobExecutionParamsInJobExecutionIds(targetIds);
        batchMetadataRepository.deleteAllStepExecutionContextInJobExecutionIds(targetIds);
        batchMetadataRepository.deleteAllStepExecutionInJobExecutionIds(targetIds);
        long actual = batchMetadataRepository.deleteAllJobExecutionInJobExecutionIds(targetIds);

        assertThat(actual).isEqualTo(1L);
        assertThat(count("select count(*) from batch_job_execution where job_execution_id = 1")).isEqualTo(0L);
        assertThat(count("select count(*) from batch_job_execution where job_execution_id = 2")).isEqualTo(1L);
    }

    @DisplayName("jobExecutionId 목록에 해당하는 batch_job_instance 삭제")
    @Test
    void test_deleteAllJobInstanceInJobExecutionIds() {
        List<Long> targetIds = List.of(1L);

        batchMetadataRepository.deleteAllJobExecutionContextInJobExecutionIds(targetIds);
        batchMetadataRepository.deleteAllJobExecutionParamsInJobExecutionIds(targetIds);
        batchMetadataRepository.deleteAllStepExecutionContextInJobExecutionIds(targetIds);
        batchMetadataRepository.deleteAllStepExecutionInJobExecutionIds(targetIds);
        List<Long> jobInstanceIds = batchMetadataRepository.selectAllJobInstanceIdsInJobExecutionIds(targetIds);
        batchMetadataRepository.deleteAllJobExecutionInJobExecutionIds(targetIds);
        long actual = batchMetadataRepository.deleteAllJobInstanceInJobInstanceIds(jobInstanceIds);

        assertThat(actual).isEqualTo(1L);
        assertThat(count("select count(*) from batch_job_execution where job_execution_id = 1")).isEqualTo(0L);
        assertThat(count("select count(*) from batch_job_instance where job_instance_id = 1")).isEqualTo(0L);
        assertThat(count("select count(*) from batch_job_instance where job_instance_id = 2")).isEqualTo(1L);
    }

    private long count(String query) {
        return ((Number) entityManager.createNativeQuery(query)
                .getSingleResult()).longValue();
    }
}
