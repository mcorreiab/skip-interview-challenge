package com.justeattakeaway.courierstatement.adapter;

import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import java.util.Optional;

public interface SaveDeliveryAdapter {
  void save(Delivery delivery);

  Optional<Delivery> findById(String deliveryId);
}
