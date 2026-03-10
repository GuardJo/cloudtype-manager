package org.github.guardjo.cloudtype.manager.model.request;

import io.swagger.v3.oas.annotations.media.Schema;

/* 특정 회원 로그아웃 요청 데이터 */
public record LogoutRequest(
        @Schema(description = "유저 아이디", example = "tester")
        String username,

        @Schema(description = "access_token", example = "access_token(JWT)")
        String accessToken,

        @Schema(description = "refresh_token", example = "refresh_token(JWT)")
        String refreshToken
) {
}
