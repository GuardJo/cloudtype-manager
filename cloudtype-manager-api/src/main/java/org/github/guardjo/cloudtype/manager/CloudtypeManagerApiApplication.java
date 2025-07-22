package org.github.guardjo.cloudtype.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "org.github.guardjo.cloudtype.manager.config.properties")
public class CloudtypeManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudtypeManagerApiApplication.class, args);
    }

}
