package com.vibrant.vibranium;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.BeforeClass;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestContainers {

    protected static final Faker FAKER = new Faker();

    @BeforeClass
    public static void beforeClass() {

        Flyway flyway = Flyway.configure().dataSource(POSTGRE_SQL_CONTAINER.getJdbcUrl(), POSTGRE_SQL_CONTAINER.getUsername(), POSTGRE_SQL_CONTAINER.getPassword()).load();
        flyway.migrate();

    }

    @Container
    public static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("ushwetam-unit-test")
            .withUsername("ushwetam")
            .withPassword("admin");

    @DynamicPropertySource
    public static void registerDataSourceProperty(DynamicPropertyRegistry dynamicPropertyRegistry){

        dynamicPropertyRegistry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);

    }

    private static DataSource getDataSourceProperty(){

        return DataSourceBuilder.create()
                .driverClassName(POSTGRE_SQL_CONTAINER.getDriverClassName())
                .url(POSTGRE_SQL_CONTAINER.getJdbcUrl())
                .username(POSTGRE_SQL_CONTAINER.getUsername())
                .password(POSTGRE_SQL_CONTAINER.getPassword())
                .build();

    }

    protected static JdbcTemplate getJDBCTemplate(){
        return new JdbcTemplate(getDataSourceProperty());
    }

}
