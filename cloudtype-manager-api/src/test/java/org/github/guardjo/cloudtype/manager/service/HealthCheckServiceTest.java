package org.github.guardjo.cloudtype.manager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(HealthCheckService.class)
class HealthCheckServiceTest {
    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private HealthCheckService healthCheckService;

    @BeforeEach
    void setUp() {
        mockServer.reset();
    }

    @DisplayName("health-check URL 호출 테스트")
    @ParameterizedTest
    @MethodSource("getIsServerActiveParams")
    void test_isServerActive(String healthCheckUrl, ResponseCreator response, boolean expected) {
        mockServer.expect(requestTo(healthCheckUrl))
                .andRespond(response);

        CompletableFuture<Boolean> actual = healthCheckService.isServerActive(healthCheckUrl);
        assertThat(actual.join()).isEqualTo(expected);
    }

    private static Stream<Arguments> getIsServerActiveParams() {
        return Stream.of(
                Arguments.of("https://success.com", withSuccess(), true),
                Arguments.of("https://failed.com", MockRestResponseCreators.withServerError(), false)
        );
    }
}