package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.database.model.DeliveryDb;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class DeliveryAdapterImpl implements DeliveryAdapter {
  private final DeliveryRepository deliveryRepository;

  public DeliveryAdapterImpl(DeliveryRepository deliveryRepository) {
    this.deliveryRepository = deliveryRepository;
  }

  public void save(Delivery delivery) {
    deliveryRepository.save(new DeliveryDb(delivery));
  }

  @Override
  public Optional<Delivery> findById(String deliveryId) {
    return deliveryRepository.findById(deliveryId)
        .map(delivery -> new Delivery(
            delivery.getId(),
            delivery.getCourierId(),
            delivery.getDate(),
            delivery.getValue()
        ));
  }
}
