package org.github.guardjo.cloudtype.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.request.LogoutRequest;
import org.github.guardjo.cloudtype.manager.model.request.RefreshTokenRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.AuthTokenInfo;

@Tag(name = "인증 관련 API", description = "인증 관련 요청 처리 API 목록")
public interface AuthApiDoc {
    @Operation(summary = "accessToken 갱신 요청", description = "주어진 refreshToken을 기반으로 accessToken을 재발급한다.")
    BaseResponse<AuthTokenInfo> refreshAccessToken(RefreshTokenRequest refreshRequest);

    @Operation(summary = "접속 계정 로그아웃 요청", description = "주어진 인증 토큰들에 대해 로그아웃 처리한다.")
    BaseResponse<String> logout(@Parameter(hidden = true) UserInfoPrincipal principal, LogoutRequest logoutRequest);
}
