package org.github.guardjo.cloudtype.manager.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchMetadataRepository {
    /**
     * 주어진 시간값 이전에 완료된 batch_job_execution 의 식별키 목록을 조회한다.
     *
     * @param dateTime 기준 시각
     * @param limit    최대 조회 개수
     * @return 해당하는 batch_job_execution 식별키 목록
     */
    List<Long> selectAllJobExecutionIdsByStatusIsCompletedAndEndTimeBefore(LocalDateTime dateTime, int limit);
}
