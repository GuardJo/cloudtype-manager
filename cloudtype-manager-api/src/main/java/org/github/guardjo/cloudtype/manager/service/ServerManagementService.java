package org.github.guardjo.cloudtype.manager.service;

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
}
