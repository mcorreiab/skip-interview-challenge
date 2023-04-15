package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AdjustmentModifiedUseCase {
  private final DeliveryAdapter deliveryAdapter;
  private final CorrectionAdapter correctionAdapter;

  public AdjustmentModifiedUseCase(DeliveryAdapter deliveryAdapter, CorrectionAdapter correctionAdapter) {
    this.deliveryAdapter = deliveryAdapter;
    this.correctionAdapter = correctionAdapter;
  }

  public void saveAdjustment(Correction correction) {
    final var delivery = deliveryAdapter.findById(correction.delivery().deliveryId());
    if (delivery.isEmpty()) {
      throw new IllegalArgumentException("Delivery not found");
    }

    correctionAdapter.save(correction.withDelivery(delivery.get()));
  }
}
