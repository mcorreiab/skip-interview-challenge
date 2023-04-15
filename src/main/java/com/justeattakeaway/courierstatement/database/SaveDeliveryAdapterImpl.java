package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.adapter.SaveDeliveryAdapter;
import com.justeattakeaway.courierstatement.database.model.CorrectionDb;
import com.justeattakeaway.courierstatement.database.model.DeliveryDb;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class SaveDeliveryAdapterImpl implements SaveDeliveryAdapter {
  private final DeliveryRepository deliveryRepository;

  public SaveDeliveryAdapterImpl(DeliveryRepository deliveryRepository) {
    this.deliveryRepository = deliveryRepository;
  }

  public void save(Delivery delivery) {
    var deliveryEntity =
        new DeliveryDb(delivery.deliveryId(),
            delivery.courierId(),
            delivery.createdTimestamp(),
            delivery.value(),
            delivery.corrections().stream().map(adjustment -> new CorrectionDb(
                adjustment.id(),
                adjustment.type(),
                adjustment.modifiedTimestamp(),
                adjustment.value()
            )).toList()
        );
    deliveryRepository.save(deliveryEntity);
  }

  @Override
  public Optional<Delivery> findById(String deliveryId) {
    return deliveryRepository.findById(deliveryId)
        .map(delivery -> new Delivery(
            delivery.deliveryId(),
            delivery.courierId(),
            delivery.createdTimestamp(),
            delivery.value(),
            delivery.adjustments().stream().map(adjustment -> new Correction(
                delivery.deliveryId(),
                adjustment.id(),
                adjustment.type(),
                adjustment.modifiedTimestamp(),
                adjustment.value()
            )).toList()
        ));
  }
}
