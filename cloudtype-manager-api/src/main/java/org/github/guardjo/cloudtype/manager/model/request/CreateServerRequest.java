package org.github.guardjo.cloudtype.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.StringUtils;

/* 서버 생성 요청 데이터 */
public record CreateServerRequest(
        @Schema(description = "서버명", example = "서버 1")
        String serverName,

        @Schema(description = "서버 URL", example = "https://naver.com")
        String serverUrl,

        @Schema(description = "서버 관리 페이지 URL", example = "https://cloudtype.io")
        String managementUrl
) {
    /**
     * 현재 인스턴스가 올바른 데이터를 지니고 있는 지 여부를 반환한다.
     *
     * @return true / false
     */
    public boolean isValid() {
        return StringUtils.hasText(this.serverName) && StringUtils.hasText(this.serverUrl) && StringUtils.hasText(this.managementUrl);
    }
}
