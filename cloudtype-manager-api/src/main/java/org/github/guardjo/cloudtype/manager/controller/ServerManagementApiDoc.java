package org.github.guardjo.cloudtype.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.request.CreateServerRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.ServerDetail;
import org.github.guardjo.cloudtype.manager.model.vo.ServerSummary;

import java.util.List;

@Tag(name = "서버 관련 API", description = "관리 서버 관련 요청 처리 API 목록")
public interface ServerManagementApiDoc {
    @Operation(summary = "서버 목록 조회", description = "관리 중인 서버에 대한 간략한 정보가 포함된 목록을 반환한다.")
    BaseResponse<List<ServerSummary>> getServers(@Parameter(hidden = true) UserInfoPrincipal principal);

    @Operation(summary = "특정 서버의 상세 정보 조회", description = "주어진 식별키에 대한 서버 상세 정보를 반환한다.")
    BaseResponse<ServerDetail> getServerDetail(Long serverId);

    @Operation(summary = "신규 서버 등록", description = "요청한 정보를 기반으로 신규 서버를 저장한다.")
    BaseResponse<String> addNewServer(@Parameter(hidden = true) UserInfoPrincipal principal, CreateServerRequest createServerRequest);
}
