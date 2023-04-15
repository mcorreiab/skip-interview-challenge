package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DeliveryFactory {

  public static Delivery createDelivery() {
    return createDelivery("deliveryId", 10.0);
  }

  public static Delivery createDelivery(String deliveryId, Double amount) {
    return new Delivery(deliveryId, "courierId", LocalDateTime.now(), BigDecimal.valueOf(amount));
  }
}
