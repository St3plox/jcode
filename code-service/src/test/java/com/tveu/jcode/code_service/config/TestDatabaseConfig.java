package com.tveu.jcode.code_service.config;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Configuration
@Testcontainers
public class TestDatabaseConfig {

    private static final PostgreSQLContainer<?> postgresContainer;

    static {
        // Initialize the PostgreSQL Testcontainers instance
        postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        postgresContainer.start();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(postgresContainer.getJdbcUrl());
        dataSource.setUsername(postgresContainer.getUsername());
        dataSource.setPassword(postgresContainer.getPassword());
        return dataSource;
    }
}
