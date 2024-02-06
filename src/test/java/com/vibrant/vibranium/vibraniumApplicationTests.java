package com.vibrant.vibranium;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class vibraniumApplicationTests {

	@Container
	private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
			.withDatabaseName("ushwetam-doa-unit-test")
			.withUsername("ushwetam")
			.withPassword("admin");

	@Test
	void canStartPostgresDB() {
		assertThat(POSTGRE_SQL_CONTAINER.isRunning()).isTrue();
		assertThat(POSTGRE_SQL_CONTAINER.isCreated()).isTrue();

	}

}
