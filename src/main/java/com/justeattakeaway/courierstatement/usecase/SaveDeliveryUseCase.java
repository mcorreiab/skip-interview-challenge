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
    deliveryAdapter.findById(delivery.deliveryId()).ifPresentOrElse(
        deliveryOnDb -> updateDelivery(delivery, deliveryOnDb),
        () -> deliveryAdapter.save(delivery)
    );
  }

  private void updateDelivery(Delivery delivery, Delivery deliveryOnDb) {
    final var deliveryToSave = new Delivery(delivery, deliveryOnDb.corrections());
    deliveryAdapter.save(deliveryToSave);
  }
}
