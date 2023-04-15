package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import org.springframework.stereotype.Service;

@Service
public class SaveDeliveryUseCase {
  private final DeliveryAdapter deliveryAdapter;

  public SaveDeliveryUseCase(DeliveryAdapter deliveryAdapter) {
    this.deliveryAdapter = deliveryAdapter;
  }

  public void saveDelivery(Delivery delivery) {
    deliveryAdapter.save(delivery);
  }
}
