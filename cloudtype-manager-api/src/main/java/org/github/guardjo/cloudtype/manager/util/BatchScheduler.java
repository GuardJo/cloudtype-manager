package org.github.guardjo.cloudtype.manager.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job updateAllServerStatusJob;

    @Scheduled(initialDelay = 10_000L, fixedDelay = 60_000L)
    public void runUpdateAllServerStatusJob() {
        log.info("Running updateAllServerStatusJob");
        
        try {
            JobExecution execution = jobLauncher.run(updateAllServerStatusJob, new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
            log.info("Finished updateAllServerStatusJob, status = {}", execution.getStatus());
        } catch (JobExecutionException e) {
            log.error("Failed execute batch job, cause = {}", e.getMessage(), e);
        }
    }
}
