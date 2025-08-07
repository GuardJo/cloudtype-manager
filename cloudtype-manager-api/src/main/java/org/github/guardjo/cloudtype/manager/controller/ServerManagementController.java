package org.github.guardjo.cloudtype.manager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.config.auth.UserInfoPrincipal;
import org.github.guardjo.cloudtype.manager.model.request.CreateServerRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.ServerDetail;
import org.github.guardjo.cloudtype.manager.model.vo.ServerSummary;
import org.github.guardjo.cloudtype.manager.service.ServerManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servers")
@Slf4j
@RequiredArgsConstructor
public class ServerManagementController implements ServerManagementApiDoc {
    private final ServerManagementService serverManagementService;

    @GetMapping
    @Override
    public BaseResponse<List<ServerSummary>> getServers(@AuthenticationPrincipal UserInfoPrincipal principal) {
        log.info("GET : /api/v1/servers, username = {}", principal.getUsername());

        List<ServerSummary> serverSummaries = serverManagementService.getServerSummaries(principal.getUserInfo());

        return BaseResponse.of(HttpStatus.OK, serverSummaries);
    }

    @GetMapping("/{serverId}")
    @Override
    public BaseResponse<ServerDetail> getServerDetail(@PathVariable("serverId") Long serverId) {
        // TODO 기능 구현 예정
        // 더미데이터 반환
        ServerDetail mockData = new ServerDetail(
                serverId,
                "서버1",
                true,
                "https://naver.com",
                "https://cloudtype.io");

        return BaseResponse.of(HttpStatus.OK, mockData);
    }

    @PostMapping
    @Override
    public BaseResponse<String> addNewServer(@AuthenticationPrincipal UserInfoPrincipal principal, @RequestBody CreateServerRequest createServerRequest) {
        log.info("POST : /api/v1/servers, username = {}", principal.getUsername());

        if (!createServerRequest.isValid()) {
            throw new IllegalArgumentException("요청 데이터가 올바르지 않습니다.");
        }

        serverManagementService.addServer(createServerRequest, principal.getUserInfo());

        return BaseResponse.defaultSuccess();
    }
}