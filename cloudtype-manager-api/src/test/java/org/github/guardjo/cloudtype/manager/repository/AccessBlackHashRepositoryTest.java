package org.github.guardjo.cloudtype.manager.repository;

import org.github.guardjo.cloudtype.manager.model.domain.AccessBlackHash;
import org.github.guardjo.cloudtype.manager.util.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataRedisTest
@Testcontainers
class AccessBlackHashRepositoryTest {
    private static final String TOKEN = "test-access-token";

    @Container
    private static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:7.4-alpine"))
                    .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }

    @Autowired
    private AccessBlackHashRepository accessBlackHashRepository;

    @AfterEach
    void tearDown() {
        accessBlackHashRepository.deleteAll();
    }

    @DisplayName("access_black_hash 내 특정 token 조회")
    @Test
    void test_findById() {
        AccessBlackHash expected = TestDataGenerator.accessBlackHash(TOKEN, 30);
        accessBlackHashRepository.save(expected);

        AccessBlackHash actual = accessBlackHashRepository.findById(TOKEN).orElseThrow();

        assertThat(actual.getToken()).isEqualTo(expected.getToken());
    }

    @DisplayName("access_black_hash에 신규 token 저장")
    @Test
    void test_save() {
        AccessBlackHash target = TestDataGenerator.accessBlackHash(TOKEN, 30);

        AccessBlackHash actual = accessBlackHashRepository.save(target);

        assertThat(actual.getToken()).isEqualTo(TOKEN);
        assertThat(accessBlackHashRepository.existsById(TOKEN)).isTrue();
    }

    @DisplayName("유효기간 만료된 토큰 삭제 여부 조회")
    @Test
    void test_expireByTtl() throws Exception {
        accessBlackHashRepository.save(TestDataGenerator.accessBlackHash(TOKEN, 1));

        long deadline = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < deadline && accessBlackHashRepository.existsById(TOKEN)) {
            Thread.sleep(200);
        }

        assertThat(accessBlackHashRepository.existsById(TOKEN)).isFalse();
    }
}
