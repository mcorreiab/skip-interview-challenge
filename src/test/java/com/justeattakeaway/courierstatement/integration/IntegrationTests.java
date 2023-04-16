package com.justeattakeaway.courierstatement.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.justeattakeaway.courierstatement.rabbitmq.model.AdjustmentModified;
import com.justeattakeaway.courierstatement.rabbitmq.model.BonusModified;
import com.justeattakeaway.courierstatement.rabbitmq.model.DeliveryCreated;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(RabbitMockConfiguration.class)
@ContextConfiguration(initializers = {IntegrationTests.Initializer.class})
@Testcontainers
@AutoConfigureMockMvc
public class IntegrationTests {
  @Container
  public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
      .withDatabaseName("postgres")
      .withUsername("integrationUser")
      .withPassword("testPass");
  @Autowired
  private MockMvc mockMvc;

  @BeforeAll
  public static void createTestData(@Autowired TestRabbitTemplate template) {
    new MockDataCreator(template).create();
  }

  @Test
  public void testCourierReport() throws Exception {
    String expectedResponse = new String(Files.readAllBytes(
        Paths.get(Objects.requireNonNull(
            getClass().getClassLoader().getResource("responses/expected_report.json")).toURI())));
    mockMvc.perform(get("/couriers/courier/report/from/2023-04-01/to/2023-04-15"))
        .andExpect(status().isOk()).andExpect(content().json(expectedResponse));
  }

  @Test
  public void courierReportShouldReturn404ForReportWithoutData() throws Exception {
    mockMvc.perform(get("/couriers/courier/report/from/2023-03-01/to/2023-03-15"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testCourierStatement() throws Exception {
    String expectedResponse = new String(Files.readAllBytes(
        Paths.get(Objects.requireNonNull(
            getClass().getClassLoader().getResource("responses/weekly_statement.json")).toURI())));
    mockMvc.perform(get("/couriers/courier/statement/week/2023-04-15"))
        .andExpect(status().isOk()).andExpect(content().json(expectedResponse));
  }

  @Test
  public void courierStatementShouldReturn404ForReportWithoutData() throws Exception {
    mockMvc.perform(get("/couriers/courier/statement/week/2023-03-15"))
        .andExpect(status().isNotFound());
  }

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
          "spring.datasource.url=" + postgres.getJdbcUrl(),
          "spring.datasource.username=" + postgres.getUsername(),
          "spring.datasource.password=" + postgres.getPassword()
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}
