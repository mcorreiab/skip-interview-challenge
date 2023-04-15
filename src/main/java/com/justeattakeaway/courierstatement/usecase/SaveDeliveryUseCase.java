package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.SaveDeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import org.springframework.stereotype.Service;

@Service
public class SaveDeliveryUseCase {
  private final SaveDeliveryAdapter saveDeliveryAdapter;

  public SaveDeliveryUseCase(SaveDeliveryAdapter saveDeliveryAdapter) {
    this.saveDeliveryAdapter = saveDeliveryAdapter;
  }

  public void saveDelivery(Delivery delivery) {
    saveDeliveryAdapter.findById(delivery.deliveryId()).ifPresentOrElse(
        deliveryOnDb -> updateDelivery(delivery, deliveryOnDb),
        () -> saveDeliveryAdapter.save(delivery)
    );
  }

  private void updateDelivery(Delivery delivery, Delivery deliveryOnDb) {
    final var deliveryToSave = new Delivery(delivery, deliveryOnDb.corrections());
    saveDeliveryAdapter.save(deliveryToSave);
  }
}
