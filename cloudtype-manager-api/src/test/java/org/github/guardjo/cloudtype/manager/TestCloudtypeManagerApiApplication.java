package org.github.guardjo.cloudtype.manager;

import org.springframework.boot.SpringApplication;

public class TestCloudtypeManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(CloudtypeManagerApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
