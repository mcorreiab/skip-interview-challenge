package com.justeattakeaway.courierstatement.usecase.exceptions;

import com.justeattakeaway.courierstatement.usecase.model.CorrectionTypes;

public class DeliveryNotFoundException extends RuntimeException {
  public DeliveryNotFoundException(String deliveryId,
                                   String correctionId,
                                   CorrectionTypes correctionType) {
    super(String.format("Delivery with id %s not found for correction with id %s and type %s",
        deliveryId, correctionId, correctionType));
  }
}
