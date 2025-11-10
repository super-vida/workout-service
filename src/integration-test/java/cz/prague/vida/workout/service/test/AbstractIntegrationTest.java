package cz.prague.vida.workout.service.test;

import java.time.Duration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;

@Tag("Integration")
@ActiveProfiles("integration-test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@AutoConfigureObservability
@ExtendWith(OutputCaptureExtension.class)
public abstract class AbstractIntegrationTest {

  static OracleContainer oracle =
      new OracleContainer(DockerImageName.parse("gvenzl/oracle-free:23"))
          .withStartupTimeout(Duration.ofMinutes(2));

  static {
    oracle.start();
  }

  @DynamicPropertySource
  static void oracleProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.r2dbc.url", AbstractIntegrationTest::r2dbcUrl);
    registry.add("spring.r2dbc.username", oracle::getUsername);
    registry.add("spring.r2dbc.password", oracle::getPassword);
    registry.add("spring.flyway.url", oracle::getJdbcUrl);
    registry.add("spring.flyway.user", oracle::getUsername);
    registry.add("spring.flyway.password", oracle::getPassword);
  }

  private static String r2dbcUrl() {
    return String.format(
        "r2dbc:oracle://%s:%s/%s",
        oracle.getHost(), oracle.getOraclePort(), oracle.getDatabaseName());
  }
}
