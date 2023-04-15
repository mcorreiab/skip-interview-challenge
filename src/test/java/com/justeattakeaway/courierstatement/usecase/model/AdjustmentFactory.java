package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdjustmentFactory {

  public static Adjustment createAdjustment() {
    return new Adjustment("deliveryId", "adjustmentId", LocalDateTime.now(), BigDecimal.valueOf(5));
  }

}
