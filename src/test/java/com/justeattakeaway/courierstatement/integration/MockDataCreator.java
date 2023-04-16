package com.justeattakeaway.courierstatement.integration;

import com.justeattakeaway.courierstatement.rabbitmq.model.AdjustmentModified;
import com.justeattakeaway.courierstatement.rabbitmq.model.BonusModified;
import com.justeattakeaway.courierstatement.rabbitmq.model.DeliveryCreated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;

public class MockDataCreator {
  private final TestRabbitTemplate template;

    public MockDataCreator(TestRabbitTemplate template) {
        this.template = template;
    }

    public void create() {
      createDeliveries();
      createAdjustments();
      createBonus();
    }

  private void createDeliveries() {
    final var deliveryInOtherWeek =
        new DeliveryCreated("outOfWeek", "courier", LocalDateTime.of(2023, 4, 8, 23, 59),
            BigDecimal.valueOf(5.5));
    template.convertSendAndReceive("deliveryCreated", deliveryInOtherWeek);

    final var deliveryWithoutAdjustments =
        new DeliveryCreated("justDelivery", "courier", LocalDateTime.of(2023, 4, 10, 22, 00),
            BigDecimal.valueOf(10));
    template.convertSendAndReceive("deliveryCreated", deliveryWithoutAdjustments);

    final var deliveryFromOtherCourier =
        new DeliveryCreated("otherCourier", "courier2", LocalDateTime.of(2023, 4, 10, 22, 00),
            BigDecimal.valueOf(10));
    template.convertSendAndReceive("deliveryCreated", deliveryFromOtherCourier);

    final var deliverWithAdjustmentsAndBonuses =
        new DeliveryCreated("deliveryWithCorrection", "courier",
            LocalDateTime.of(2023, 4, 10, 23, 59),
            BigDecimal.valueOf(10));
    template.convertSendAndReceive("deliveryCreated", deliverWithAdjustmentsAndBonuses);
  }

  private void createAdjustments() {
    final var adjustmentFromOtherCourier =
        new AdjustmentModified("otherCourierAdjustment", "otherCourier",
            LocalDateTime.of(2023, 4, 14, 19, 0),
            BigDecimal.valueOf(15.4));
    template.convertSendAndReceive("adjustmentModified", adjustmentFromOtherCourier);

    final var adjustment =
        new AdjustmentModified("validAdjustment", "deliveryWithCorrection",
            LocalDateTime.of(2023, 4, 14, 19, 0),
            BigDecimal.valueOf(-5));
    template.convertSendAndReceive("adjustmentModified", adjustment);
  }

  private void createBonus() {
    final var bonusModified =
        new BonusModified("bonus", "deliveryWithCorrection", LocalDateTime.of(2023, 4, 12, 23, 0),
            BigDecimal.valueOf(4));
    template.convertSendAndReceive("bonusModified", bonusModified);
  }
}
