package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record Delivery(
    String deliveryId,
    String courierId,
    LocalDateTime createdTimestamp,
    BigDecimal value,
    List<Adjustment> adjustments) {
  public Delivery(String deliveryId, String courierId, LocalDateTime createdTimestamp,
                  BigDecimal value) {
    this(deliveryId, courierId, createdTimestamp, value, new ArrayList<>());
  }

  public Delivery(String deliveryId, List<Adjustment> adjustments) {
    this(deliveryId, null, null, null, adjustments);
  }

  public Delivery(Delivery delivery, List<Adjustment> adjustments) {
    this(delivery.deliveryId, delivery.courierId, delivery.createdTimestamp, delivery.value,
        adjustments);
  }
}
