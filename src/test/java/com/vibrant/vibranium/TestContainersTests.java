package com.vibrant.vibranium;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class TestContainersTests extends AbstractTestContainers {

	@Test
	void canStartPostgresDB() {
		assertThat(POSTGRE_SQL_CONTAINER.isRunning()).isTrue();
		assertThat(POSTGRE_SQL_CONTAINER.isCreated()).isTrue();

	}

}
