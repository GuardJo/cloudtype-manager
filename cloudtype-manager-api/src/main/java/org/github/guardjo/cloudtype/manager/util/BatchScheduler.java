package org.github.guardjo.cloudtype.manager.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job updateAllServerStatusJob;

    @Scheduled(initialDelay = 10_000L, fixedDelay = 60_000L)
    public void runUpdateAllServerStatusJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("Running updateAllServerStatusJob");

        JobExecution execution = jobLauncher.run(updateAllServerStatusJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters());

        log.info("Finished updateAllServerStatusJob, status = {}", execution.getStatus());
    }
}
