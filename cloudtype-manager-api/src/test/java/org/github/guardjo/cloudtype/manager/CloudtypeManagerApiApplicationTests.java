package org.github.guardjo.cloudtype.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CloudtypeManagerApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
