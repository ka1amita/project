package com.gfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication()
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    // @Bean
    // FlywayMigrationStrategy flywayMigrationStrategy() {
    //     prevents automatic migration on App start-up
        // return args -> {};
    // }
}
