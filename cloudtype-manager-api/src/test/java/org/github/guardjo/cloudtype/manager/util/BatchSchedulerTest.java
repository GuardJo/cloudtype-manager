package org.github.guardjo.cloudtype.manager.util;

import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMost;

@SpringBatchTest
@SpringBootTest
@Sql(scripts = "classpath:org/springframework/batch/core/schema-h2.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BatchSchedulerTest {
    private final static UserInfoEntity TEST_USER = TestDataGenerator.userInfoEntity("Tester");
    private final static List<ServerInfoEntity> SERVER_INFOS = new ArrayList<>();

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job updateAllServerStatusJob;

    @Autowired
    private UserInfoEntityRepository userInfoEntityRepository;

    @Autowired
    private ServerInfoEntityRepository serverInfoEntityRepository;

    @MockitoBean
    private HealthCheckService healthCheckService;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils.setJob(updateAllServerStatusJob);

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

    @DisplayName("updateAllServerStatusJob 배치 수행")
    @Test
    void test_runUpdateAllServerStatusJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        given(healthCheckService.isServerActive(anyString())).willReturn(CompletableFuture.completedFuture(true));

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        then(healthCheckService).should(atMost(SERVER_INFOS.size())).isServerActive(anyString());
    }
}