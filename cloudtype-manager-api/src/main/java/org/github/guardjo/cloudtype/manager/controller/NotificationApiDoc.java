package org.github.guardjo.cloudtype.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.request.AppPushTokenRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;

@Tag(name = "알림 관련 API", description = "알림 관련 요청 처리 API 목록")
public interface NotificationApiDoc {
    @Operation(summary = "앱푸시 토큰 저장", description = "요청 회원에 대한 디바이스 정보 및 푸시알림 토큰을 저장한다.")
    BaseResponse<String> addPushToken(@Parameter(hidden = true) UserInfoPrincipal principal, AppPushTokenRequest request);
}
