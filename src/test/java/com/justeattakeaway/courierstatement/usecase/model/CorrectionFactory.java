package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CorrectionFactory {

  public static Correction createAdjustment() {
    return new Correction(
        "deliveryId",
        DeliveryFactory.createDelivery(),
        CorrectionTypes.ADJUSTMENT,
        LocalDateTime.now(),
        BigDecimal.valueOf(5)
    );
  }

  public static Correction createBonus() {
    return new Correction(
        "deliveryId",
        DeliveryFactory.createDelivery(),
        CorrectionTypes.BONUS,
        LocalDateTime.now(),
        BigDecimal.valueOf(12)
    );
  }

}
