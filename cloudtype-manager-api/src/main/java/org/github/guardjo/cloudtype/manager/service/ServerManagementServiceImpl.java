package org.github.guardjo.cloudtype.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.domain.ServerInfoEntity;
import org.github.guardjo.cloudtype.manager.model.vo.ServerSummary;
import org.github.guardjo.cloudtype.manager.model.vo.UserInfo;
import org.github.guardjo.cloudtype.manager.repository.ServerInfoEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerManagementServiceImpl implements ServerManagementService {
    private final ServerInfoEntityRepository serverInfoRepository;

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
}
