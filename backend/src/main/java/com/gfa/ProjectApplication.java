package com.gfa;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
        // Create the Flyway instance and point it to the database
        Flyway flyway = Flyway.configure().dataSource("jdbc:mysql://localhost:3306/fox-dining?serverTimezone=UTC", "root", "password").load();
flyway.getConfiguration();
        // Start the migration
        flyway.migrate();
    }
}
