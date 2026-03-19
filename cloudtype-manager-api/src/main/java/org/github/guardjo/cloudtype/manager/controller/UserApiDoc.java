package org.github.guardjo.cloudtype.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.request.CustomerInquiryRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;

@Tag(name = "회원 관련 API", description = "회원 관련 요청 처리 API 목록")
public interface UserApiDoc {
    @Operation(summary = "본인 정보 조회", description = "현재 회원 당사자 정보를 반환한다.")
    BaseResponse<UserInfo> getMyInfo(@Parameter(hidden = true) UserInfoPrincipal principal);

    @Operation(summary = "앱푸시 토큰 조회", description = "현재 회원 당사자의 앱푸시 토큰을 반환한다.")
    BaseResponse<String> getMyFCMToken(@Parameter(hidden = true) UserInfoPrincipal principal, @Parameter(description = "디바이스 식별키", required = true) String deviceId);

    @Operation(summary = "고객 문의 메일 발송", description = "현재 회원이 작성한 고객문의 사항을 메일로 송신한다.")
    BaseResponse<String> sendInquiry(@Parameter(hidden = true) UserInfoPrincipal principal, CustomerInquiryRequest inquiryRequest);
}
