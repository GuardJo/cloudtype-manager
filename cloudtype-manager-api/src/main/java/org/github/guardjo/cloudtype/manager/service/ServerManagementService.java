package org.github.guardjo.cloudtype.manager.service;

import org.github.guardjo.cloudtype.manager.model.request.CreateServerRequest;
import org.github.guardjo.cloudtype.manager.model.vo.ServerSummary;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;

import java.util.List;

public interface ServerManagementService {

    /**
     * 해당 UserInfo에 연관된 ServerSummary 목록을 반환한다.
     *
     * @param userInfo 계정 정보 VO
     * @return ServerSummary 목록
     */
    List<ServerSummary> getServerSummaries(UserInfo userInfo);

    /**
     * 주어진 데이터를 기반으로 신규 server를 등록한다.
     *
     * @param createRequest 신규 server 등록 정보
     */
    void addServer(CreateServerRequest createRequest, UserInfo userInfo);
}
