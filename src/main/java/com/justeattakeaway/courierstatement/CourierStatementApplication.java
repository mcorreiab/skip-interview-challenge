package com.justeattakeaway.courierstatement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({ "com.justeattakeaway.courierstatement" })
public class CourierStatementApplication {

  public static void main(String[] args) {
    SpringApplication.run(CourierStatementApplication.class, args);
  }

}
