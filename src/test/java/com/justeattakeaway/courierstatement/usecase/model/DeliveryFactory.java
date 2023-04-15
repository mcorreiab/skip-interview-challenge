package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DeliveryFactory {

  public static Delivery createDelivery() {
    return new Delivery("deliveryId", "courierId", LocalDateTime.now(), BigDecimal.valueOf(10));
  }
}
