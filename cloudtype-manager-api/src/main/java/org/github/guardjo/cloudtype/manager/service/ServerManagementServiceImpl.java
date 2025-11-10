package org.github.guardjo.cloudtype.manager.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.model.request.CreateServerRequest;
import org.github.guardjo.cloudtype.manager.model.vo.ServerDetail;
import org.github.guardjo.cloudtype.manager.model.vo.ServerSummary;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerManagementServiceImpl implements ServerManagementService {
    private final ServerInfoEntityRepository serverInfoRepository;
    private final UserInfoEntityRepository userInfoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ServerSummary> getServerSummaries(UserInfo userInfo) {
        String username = userInfo.id();

        List<ServerInfoEntity> serverInfoEntities = serverInfoRepository.findAllByUserInfo_Username(username);

        log.debug("Found all serverInfoEntities, username = {}, size = {}", username, serverInfoEntities.size());

        return serverInfoEntities.stream()
                .map(ServerSummary::of)
                .toList();
    }

    @Override
    @Transactional
    public void addServer(CreateServerRequest createRequest, UserInfo userInfo) {
        log.debug("Add new server, serverName = {}, username = {}", createRequest.serverName(), userInfo.id());

        ServerInfoEntity newServer = generateServerInfoEntity(createRequest, userInfo);

        newServer = serverInfoRepository.save(newServer);

        log.debug("Added new servere, serverId = {}", newServer.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ServerDetail getServerDetail(Long serverId, UserInfo userInfo) {
        log.debug("Find server detail, serverId = {}, username = {}", serverId, userInfo.id());

        ServerInfoEntity serverInfoEntity = serverInfoRepository.findByIdAndUserInfo_Username(serverId, userInfo.id())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Not Found ServerInfoEntity, serverId = %d, username = %s", serverId, userInfo.id())));

        return ServerDetail.from(serverInfoEntity);
    }

    @Override
    @Transactional
    public void deleteMyServer(Long serverId, UserInfo userInfo) {
        log.debug("Delete server, serverId = {}, username = {}", serverId, userInfo.id());

        ServerInfoEntity serverInfoEntity = serverInfoRepository.findById(serverId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Not Found ServerInfoEntity, serverId = %d", serverId)));

        if (!serverInfoEntity.getUserInfo().getUsername().equals(userInfo.id())) {
            log.warn("Not allowed to delete server, serverId = {}, username = {}", serverId, userInfo.id());
            throw new AccessDeniedException(String.format("Not allowed to delete server, serverId = %d, username = %s", serverId, userInfo.id()));

        }

        serverInfoRepository.delete(serverInfoEntity);

        log.debug("Deleted server, serverId = {}", serverInfoEntity.getId());
    }

    /*
    신규 ServerInfo Entity 인스턴스 생성
     */
    private ServerInfoEntity generateServerInfoEntity(CreateServerRequest createServerRequest, UserInfo userInfo) {
        UserInfoEntity userInfoEntity = userInfoRepository.getReferenceById(userInfo.id());

        return ServerInfoEntity.builder()
                .serverName(createServerRequest.serverName())
                .hostingUrl(createServerRequest.serverUrl())
                .healthCheckUrl(createServerRequest.serverUrl())
                .managementUrl(createServerRequest.managementUrl())
                .userInfo(userInfoEntity)
                .build();
    }
}
