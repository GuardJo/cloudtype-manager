package org.github.guardjo.cloudtype.manager.service;

import org.github.guardjo.cloudtype.manager.model.request.CreateServerRequest;
import org.github.guardjo.cloudtype.manager.model.vo.ServerDetail;
import org.github.guardjo.cloudtype.manager.model.vo.ServerStatusChangeHistorySummary;
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

    /**
     * 해당 UserInfo에 연관된 Server 목록 중 serverId에 해당하는 server에 대한 상세 정보를 조회한다.
     *
     * @param serverId 서버 식별키
     * @param userInfo 계정 정보 VO
     * @return server 상세 정보
     */
    ServerDetail getServerDetail(Long serverId, UserInfo userInfo);

    /**
     * 주어진 식별키에 해당하는 server 정보를 삭제한다.
     * <hr/>
     * 단, 해당 서버를 저장한 회원이 아닐 경우, 삭제 요청을 거절한다.
     *
     * @param serverId 서버 식별키
     * @param userInfo 계정 정보 VO
     */
    void deleteMyServer(Long serverId, UserInfo userInfo);

    /**
     * 주어진 회원 식별키에 해당하는 회원이 관리 중인 서버의 상태 변경 이력 목록을 조회한다.
     * <hr/>
     * <i>이 때, 인자로 주어진 페이지 숫자와 페이지 크기를 기반으로 페이지네이션 처리</i>
     *
     * @param userId     회원 아이디
     * @param pageNumber 페이지 수
     * @param pageSize   페이키 크기
     * @return 서버 상태 변경 이력 목록
     */
    List<ServerStatusChangeHistorySummary> findAllServerStatusChangeHistories(String userId, int pageNumber, int pageSize);
}
