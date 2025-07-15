package org.github.guardjo.cloudtype.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.request.CreateServerRequest;
import org.github.guardjo.cloudtype.manager.model.response.BaseResponse;
import org.github.guardjo.cloudtype.manager.model.vo.ServerDetail;
import org.github.guardjo.cloudtype.manager.model.vo.ServerSummary;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servers")
@Slf4j
public class ServerManagementController implements ServerManagementApiDoc {
    @GetMapping
    @Override
    public BaseResponse<List<ServerSummary>> getServers() {
        // TODO 기능 구현 예정
        // 더미 데이터 반환
        ServerSummary serverSummary = new ServerSummary(
                1L,
                "서버 1",
                true
        );

        return BaseResponse.of(HttpStatus.OK, List.of(serverSummary));
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
    public BaseResponse<String> addNewServer(@RequestBody CreateServerRequest createServerRequest) {
        // TODO 기능 구현 예정

        return BaseResponse.defaultSuccess();
    }
}