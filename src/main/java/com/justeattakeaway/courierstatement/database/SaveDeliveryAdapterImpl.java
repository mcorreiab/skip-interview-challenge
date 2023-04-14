package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.adapter.SaveDeliveryAdapter;
import com.justeattakeaway.courierstatement.database.model.Delivery;
import com.justeattakeaway.courierstatement.rabbitmq.DeliveryCreated;
import org.springframework.stereotype.Component;

@Component
public class SaveDeliveryAdapterImpl implements SaveDeliveryAdapter {
  private final DeliveryRepository deliveryRepository;

  public SaveDeliveryAdapterImpl(DeliveryRepository deliveryRepository) {
    this.deliveryRepository = deliveryRepository;
  }

  public void save(DeliveryCreated delivery) {
    var deliveryEntity =
        new Delivery(delivery.deliveryId(),
            delivery.courierId(),
            delivery.createdTimestamp(),
            delivery.value()
        );
    deliveryRepository.save(deliveryEntity);
  }
}
