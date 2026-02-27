package org.github.guardjo.cloudtype.manager.repository;

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
}