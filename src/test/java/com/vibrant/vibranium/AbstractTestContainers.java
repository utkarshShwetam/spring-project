package com.vibrant.vibranium;

import org.flywaydb.core.Flyway;
import org.junit.BeforeClass;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestContainers {

    @BeforeClass
    static void beforeClass() {
        Flyway flyway = Flyway.configure().dataSource(POSTGRE_SQL_CONTAINER.getJdbcUrl(), POSTGRE_SQL_CONTAINER.getUsername(), POSTGRE_SQL_CONTAINER.getPassword()).load();
        flyway.migrate();

    }

    @DynamicPropertySource
    public static void registerDataSourceProperty(DynamicPropertyRegistry dynamicPropertyRegistry){

        dynamicPropertyRegistry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);

    }

    @Container
    public static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("ushwetam-doa-unit-test")
            .withUsername("ushwetam")
            .withPassword("admin");

}
