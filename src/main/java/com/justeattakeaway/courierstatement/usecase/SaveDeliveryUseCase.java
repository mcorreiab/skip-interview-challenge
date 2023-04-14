package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.SaveDeliveryAdapter;
import com.justeattakeaway.courierstatement.rabbitmq.DeliveryCreated;
import org.springframework.stereotype.Service;

@Service
public class SaveDeliveryUseCase {
  private final SaveDeliveryAdapter saveDeliveryAdapter;

  public SaveDeliveryUseCase(SaveDeliveryAdapter saveDeliveryAdapter) {
    this.saveDeliveryAdapter = saveDeliveryAdapter;
  }

  public void saveDelivery(DeliveryCreated deliveryCreated) {
    saveDeliveryAdapter.save(deliveryCreated);
  }
}
