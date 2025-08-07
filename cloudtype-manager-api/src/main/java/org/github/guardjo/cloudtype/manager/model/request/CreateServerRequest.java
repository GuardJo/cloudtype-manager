package org.github.guardjo.cloudtype.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/* 서버 생성 요청 데이터 */
public record CreateServerRequest(
        @Schema(description = "서버명", example = "서버 1")
        @NotBlank(message = "서버명을 입력해주세요.")
        String serverName,

        @Schema(description = "서버 URL", example = "https://naver.com")
        @NotBlank(message = "서버 URL을 입력해주세요.")
        String serverUrl,

        @Schema(description = "서버 관리 페이지 URL", example = "https://cloudtype.io")
        @NotBlank(message = "서버 관리자 URL을 입력해주세요.")
        String managementUrl
) {
}
