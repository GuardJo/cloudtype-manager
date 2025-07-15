package org.github.guardjo.cloudtype.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;

/* 서버 생성 요청 데이터 */
public record CreateServerRequest(
        @Schema(description = "서버명", example = "서버 1")
        String serverName,

        @Schema(description = "서버 URL", example = "https://naver.com")
        String serverUrl,

        @Schema(description = "서버 관리 페이지 URL", example = "https://cloudtype.io")
        String managementUrl
) {
}
