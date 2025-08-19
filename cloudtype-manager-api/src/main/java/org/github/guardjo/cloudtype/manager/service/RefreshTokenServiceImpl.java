package org.github.guardjo.cloudtype.manager.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.guardjo.cloudtype.manager.model.domain.RefreshTokenEntity;
import org.github.guardjo.cloudtype.manager.model.domain.UserInfoEntity;
import org.github.guardjo.cloudtype.manager.repository.RefreshTokenEntityRepository;
import org.github.guardjo.cloudtype.manager.repository.UserInfoEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserInfoEntityRepository userInfoRepository;
    private final RefreshTokenEntityRepository refreshTokenRepository;

    @Override
    @Transactional
    public void saveNewToken(String token, String username) {
        log.debug("Creating refresh-token, username = {}", username);

        UserInfoEntity userInfo = userInfoRepository.getReferenceById(username);

        if (Objects.isNull(userInfo)) {
            log.error("Not Found user_info, username = {}", username);
            throw new EntityNotFoundException(String.format("Not found user_info, username = %s", username));
        }

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(token)
                .userInfo(userInfo)
                .build();

        refreshTokenRepository.save(refreshToken);

        log.info("Saved refresh-token, username = {}", username);
    }
}
