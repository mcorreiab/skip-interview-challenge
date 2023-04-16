package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.exceptions.DeliveryNotFoundException;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import org.springframework.stereotype.Service;

@Service
public class CorrectionModifiedUseCase {
  private final DeliveryAdapter deliveryAdapter;
  private final CorrectionAdapter correctionAdapter;

  public CorrectionModifiedUseCase(DeliveryAdapter deliveryAdapter,
                                   CorrectionAdapter correctionAdapter) {
    this.deliveryAdapter = deliveryAdapter;
    this.correctionAdapter = correctionAdapter;
  }

  public void saveCorrection(Correction correction) {
    final var delivery = deliveryAdapter.findById(correction.delivery().deliveryId());
    if (delivery.isEmpty()) {
      throw new DeliveryNotFoundException(correction.delivery().deliveryId(), correction.id(),
          correction.type());
    }

    correctionAdapter.save(correction.withDelivery(delivery.get()));
  }
}
