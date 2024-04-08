package cloud.deuterium.wb.it;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseIT {

    @Container
    final static PostgreSQLContainer<?> POSTGRE_SQL = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("cpv_textil-test")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    private static void registerProperties(DynamicPropertyRegistry registry) {
        POSTGRE_SQL.start();
        registry.add("spring.datasource.url", POSTGRE_SQL::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL::getPassword);
    }
}
