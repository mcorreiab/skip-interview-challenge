package com.justeattakeaway.courierstatement.usecase.model;

import com.justeattakeaway.courierstatement.database.model.CorrectionDb;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Delivery(
    String deliveryId,
    String courierId,
    LocalDateTime createdTimestamp,
    BigDecimal value) {
  public Delivery(String deliveryId) {
    this(deliveryId, null, null, null);
  }
}
