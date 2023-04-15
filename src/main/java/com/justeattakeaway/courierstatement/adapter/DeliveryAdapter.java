package com.justeattakeaway.courierstatement.adapter;

import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import java.util.Optional;

public interface DeliveryAdapter {
  void save(Delivery delivery);

  Optional<Delivery> findById(String deliveryId);
}
