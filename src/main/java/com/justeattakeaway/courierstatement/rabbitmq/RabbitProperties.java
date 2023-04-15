package com.justeattakeaway.courierstatement.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rabbitmq")
public record RabbitProperties(Queues queues) {

  public record Queues(String deliveryCreated, String adjustmentModified, String bonusModified) {
  }
}
