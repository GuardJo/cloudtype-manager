package org.github.guardjo.cloudtype.manager.model.vo;

/* 서버 상태 체크 결과 VO */
public record HealthCheckResult(
        Long serverId,
        boolean activate
) {
}
