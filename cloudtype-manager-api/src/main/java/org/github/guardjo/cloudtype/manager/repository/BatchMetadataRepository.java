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

    /**
     * 주어진 jobExecutionId 목록에 포함된 컬럼 값이 존재하는 batch_job_execution_context 들을 삭제한다.
     *
     * @param jobExecutionIds batch_job_execution 식별키 목록
     * @return 삭제된 row 수
     */
    long deleteAllJobExecutionContextInJobExecutionIds(List<Long> jobExecutionIds);

    /**
     * 주어진 jobExecutionId 목록에 포함된 컬럼 값이 존재하는 batch_job_execution_params 들을 삭제한다.
     *
     * @param jobExecutionIds batch_job_execution 식별키 목록
     * @return 삭제된 row 수
     */
    long deleteAllJobExectionParamsInJobExecutionIds(List<Long> jobExecutionIds);

    /**
     * 주어진 jobExecutionId 목록에 포함된 컬럼 값이 존재하는 batch_step_execution 들을 삭제한다.
     *
     * @param jobExecutionIds batch_job_execution 식별키 목록
     * @return 삭제된 row 수
     */
    long deleteAllStepExecutionInJobExecutionIds(List<Long> jobExecutionIds);

    /**
     * 주어진 jobExecutionId 목록에 포함된 컬럼 값이 존재하는 batch_step_execution_context 들을 삭제한다.
     *
     * @param jobExecutionIds batch_job_execution 식별키 목록
     * @return 삭제된 row 수
     */
    long deleteAllStepExecutionContextInJobExecutionIds(List<Long> jobExecutionIds);

    /**
     * 주어진 jobExecutionId 목록에 포함된 컬럼 값이 존재하는 batch_job_execution 들을 삭제한다.
     *
     * @param jobExecutionIds batch_job_execution 식별키 목록
     * @return 삭제된 row 수
     */
    long deleteAllJobExecutionInJobExecutionIds(List<Long> jobExecutionIds);

    /**
     * 주어진 jobExecutionId 목록에 대응되는 batch_job_instance 들을 삭제한다.
     * batch_job_execution 참조가 남아있는 경우는 삭제 대상에서 제외한다.
     *
     * @param jobExecutionIds batch_job_execution 식별키 목록
     * @return 삭제된 row 수
     */
    long deleteAllJobInstanceInJobExecutionIds(List<Long> jobExecutionIds);
}
