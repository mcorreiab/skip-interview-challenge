package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CorrectionFactory {

  public static Correction createAdjustment() {
    return createAdjustment(
        "deliveryId",
        5.0,
        DeliveryFactory.createDelivery()
    );
  }

  public static Correction createAdjustment(String id, Double amount, Delivery delivery) {
    return new Correction(
        id,
        delivery,
        CorrectionTypes.ADJUSTMENT,
        LocalDateTime.now(),
        BigDecimal.valueOf(amount)
    );
  }


  public static Correction createBonus() {
    return createBonus(
        "deliveryId",
        12.0,
        DeliveryFactory.createDelivery()
    );
  }

  public static Correction createBonus(String id, Double amount, Delivery delivery) {
    return new Correction(
        id,
        delivery,
        CorrectionTypes.BONUS,
        LocalDateTime.now(),
        BigDecimal.valueOf(amount)
    );
  }

}
