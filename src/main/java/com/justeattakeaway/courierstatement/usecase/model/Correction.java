package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Correction(
    String id,
    Delivery delivery,
    CorrectionTypes type,
    LocalDateTime modifiedTimestamp,
    BigDecimal value) {
  public Correction withDelivery(Delivery delivery) {
    return new Correction(id, delivery, type, modifiedTimestamp, value);
  }
}
