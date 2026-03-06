package org.github.guardjo.cloudtype.manager.util;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.repository.BatchMetadataRepository;
import org.github.guardjo.cloudtype.manager.repository.RefreshTokenEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.service.HealthCheckService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBatchTest
@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BatchSchedulerTest {
    private final static UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");
    private final static List<ServerInfoEntity> SERVER_INFOS = new ArrayList<>();

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job updateAllServerStatusJob;

    @Autowired
    private Job cleanupRefreshTokenJob;

    @Autowired
    private Job cleanupBatchMetadataJob;

    @Autowired
    private UserInfoEntityRepository userInfoEntityRepository;

    @Autowired
    private ServerInfoEntityRepository serverInfoEntityRepository;

    @Autowired
    private DataSource dataSource;

    @MockitoBean
    private HealthCheckService healthCheckService;

    @MockitoBean
    private RefreshTokenEntityRepository refreshTokenEntityRepository;

    @MockitoBean
    private BatchMetadataRepository batchMetadataRepository;

    @BeforeEach
    void setUp() {
        initializeBatchSchemaIfMissing();
        userInfoEntityRepository.save(TEST_USER);

        for (int i = 0; i < 10; i++) {
            SERVER_INFOS.add(TestDataGenerator.serverInfoEntity("Server" + i, TEST_USER));
        }

        serverInfoEntityRepository.saveAll(SERVER_INFOS);
    }

    @AfterEach
    void tearDown() {
        serverInfoEntityRepository.deleteAll();
        userInfoEntityRepository.deleteAll();
        SERVER_INFOS.clear();
    }

    private void initializeBatchSchemaIfMissing() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String relationName = jdbcTemplate.queryForObject("select to_regclass('batch_job_instance')", String.class);

        if (relationName == null) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                    new ClassPathResource("org/springframework/batch/core/schema-postgresql.sql"));
            populator.execute(dataSource);
        }
    }

    @DisplayName("updateAllServerStatusJob 배치 수행")
    @Test
    void test_runUpdateAllServerStatusJob() throws Exception {
        jobLauncherTestUtils.setJob(updateAllServerStatusJob);
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        given(healthCheckService.isServerActive(any(ServerInfoEntity.class))).willReturn(CompletableFuture.completedFuture(true));

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        then(healthCheckService).should(times(SERVER_INFOS.size())).isServerActive(any(ServerInfoEntity.class));
    }

    @DisplayName("cleanupRefreshTokenJob 배치 수행")
    @Test
    void test_cleanupRefreshTokenJob() throws Exception {
        jobLauncherTestUtils.setJob(cleanupRefreshTokenJob);
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        given(refreshTokenEntityRepository.deleteAllByModifiedAtBefore(any(LocalDateTime.class))).willReturn(10);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        then(refreshTokenEntityRepository).should().deleteAllByModifiedAtBefore(any(LocalDateTime.class));
    }

    @DisplayName("cleanupBatchMetadataJob 배치 수행")
    @Test
    void test_cleanupBatchMetadataJob() throws Exception {
        jobLauncherTestUtils.setJob(cleanupBatchMetadataJob);
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        List<Long> targetExecutionIds = List.of(1L, 2L);

        given(batchMetadataRepository.selectAllJobExecutionIdsByStatusIsCompletedAndEndTimeBefore(any(LocalDateTime.class), any(Integer.class)))
                .willReturn(targetExecutionIds);
        given(batchMetadataRepository.deleteAllStepExecutionContextInJobExecutionIds(targetExecutionIds)).willReturn(2L);
        given(batchMetadataRepository.deleteAllStepExecutionInJobExecutionIds(targetExecutionIds)).willReturn(2L);
        given(batchMetadataRepository.deleteAllJobExecutionContextInJobExecutionIds(targetExecutionIds)).willReturn(2L);
        given(batchMetadataRepository.deleteAllJobExecutionParamsInJobExecutionIds(targetExecutionIds)).willReturn(2L);
        given(batchMetadataRepository.selectAllJobInstanceIdsInJobExecutionIds(targetExecutionIds)).willReturn(targetExecutionIds);
        given(batchMetadataRepository.deleteAllJobExecutionInJobExecutionIds(targetExecutionIds)).willReturn(2L);
        given(batchMetadataRepository.deleteAllJobInstanceInJobInstanceIds(targetExecutionIds)).willReturn(2L);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        then(batchMetadataRepository).should().selectAllJobExecutionIdsByStatusIsCompletedAndEndTimeBefore(any(LocalDateTime.class), any(Integer.class));
        then(batchMetadataRepository).should().selectAllJobInstanceIdsInJobExecutionIds(targetExecutionIds);
        then(batchMetadataRepository).should().deleteAllStepExecutionContextInJobExecutionIds(targetExecutionIds);
        then(batchMetadataRepository).should().deleteAllStepExecutionInJobExecutionIds(targetExecutionIds);
        then(batchMetadataRepository).should().deleteAllJobExecutionContextInJobExecutionIds(targetExecutionIds);
        then(batchMetadataRepository).should().deleteAllJobExecutionParamsInJobExecutionIds(targetExecutionIds);
        then(batchMetadataRepository).should().deleteAllJobExecutionInJobExecutionIds(targetExecutionIds);
        then(batchMetadataRepository).should().deleteAllJobInstanceInJobInstanceIds(targetExecutionIds);
    }
}
