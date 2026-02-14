package org.github.guardjo.cloudtype.manager.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.vo.HealthCheckResult;
import org.github.guardjo.cloudtype.manager.repository.RefreshTokenEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.service.HealthCheckService;
import org.github.guardjo.cloudtype.manager.service.NotificationService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {
    private final static int CHUNK_SIZE = 1_000;
    private final static long CLEAR_REFRESH_TOKEN_EXPIRED_WEEK = 1L; // refresh-token의 만료 기한 (1주일)

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final HealthCheckService healthCheckService;
    private final NotificationService notificationService;
    private final ServerInfoEntityRepository serverInfoRepository;
    private final TaskExecutor healthCheckExecutor;
    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    @Bean
    public Job updateAllServerStatusJob() {
        return new JobBuilder("updateAllServerStatusJob", jobRepository)
                .start(updateAllServerStatusStep())
                .build();
    }

    @Bean
    public Job cleanupRefreshTokenJob() {
        return new JobBuilder("cleanupRefreshTokenJob", jobRepository)
                .start(cleanUpRefreshTokenStep())
                .build();
    }

    @Bean
    public Step updateAllServerStatusStep() {
        return new StepBuilder("updateAllServerStatusStep", jobRepository)
                .<ServerInfoEntity, Future<HealthCheckResult>>chunk(CHUNK_SIZE, transactionManager)
                .reader(serverInfoItemReader())
                .processor(serverInfoAsyncItemProcessor())
                .writer(serverInfoAsyncItemWriter())
                .build();
    }

    @Bean
    public Step cleanUpRefreshTokenStep() {
        return new StepBuilder("cleanUpRefreshTokenStep", jobRepository)
                .tasklet(cleanupRefreshTokenTasklet(), transactionManager)
                .build();
    }

    @Bean
    public ItemReader<ServerInfoEntity> serverInfoItemReader() {
        return new RepositoryItemReaderBuilder<ServerInfoEntity>()
                .name("serverInfoItemReader")
                .repository(serverInfoRepository)
                .methodName("findAll")
                .pageSize(CHUNK_SIZE)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public AsyncItemProcessor<ServerInfoEntity, HealthCheckResult> serverInfoAsyncItemProcessor() {
        AsyncItemProcessor<ServerInfoEntity, HealthCheckResult> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(serverInfoItemProcessor());
        asyncItemProcessor.setTaskExecutor(healthCheckExecutor);

        return asyncItemProcessor;
    }

    @Bean
    public ItemProcessor<ServerInfoEntity, HealthCheckResult> serverInfoItemProcessor() {
        return (server) -> {
            boolean newStatus = healthCheckService.isServerActive(server.getHealthCheckUrl()).join();

            if (server.isActivate() != newStatus) {
                return new HealthCheckResult(server.getId(), newStatus);
            }

            return null;
        };
    }

    @Bean
    public AsyncItemWriter<HealthCheckResult> serverInfoAsyncItemWriter() {
        AsyncItemWriter<HealthCheckResult> serverInfoAsyncItemWriter = new AsyncItemWriter<>();
        serverInfoAsyncItemWriter.setDelegate(serverInfoItemWriter());

        return serverInfoAsyncItemWriter;
    }

    @Bean
    public ItemWriter<HealthCheckResult> serverInfoItemWriter() {
        return (results) -> {
            Map<Boolean, List<Long>> partitionedIds = results.getItems().stream()
                    .collect(Collectors.partitioningBy(HealthCheckResult::activate,
                            Collectors.mapping(HealthCheckResult::serverId, Collectors.toList())));

            List<Long> activateIds = partitionedIds.getOrDefault(true, List.of());
            List<Long> deactivateIds = partitionedIds.getOrDefault(false, List.of());

            long updateResult = serverInfoRepository.updateActivateStatus(activateIds, deactivateIds);

            notificationService.sendServerInactiveNotification(deactivateIds);

            log.info("Updated server_info activate fields, updateCount = {}", updateResult);
        };
    }

    @Bean
    public Tasklet cleanupRefreshTokenTasklet() {
        return ((contribution, chunkContext) -> {
            LocalDateTime expiredAt = LocalDateTime.now().minusWeeks(CLEAR_REFRESH_TOKEN_EXPIRED_WEEK);
            int deletedRows = refreshTokenEntityRepository.deleteAllByModifiedAtBefore(expiredAt);

            log.info("Cleanup expired refreshTokens, deletedRows = {}", deletedRows);

            return RepeatStatus.FINISHED;
        });
    }
}
