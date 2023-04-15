package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionTypes;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AdjustmentModifiedUseCase {
  private final DeliveryAdapter deliveryAdapter;

  public AdjustmentModifiedUseCase(DeliveryAdapter deliveryAdapter) {
    this.deliveryAdapter = deliveryAdapter;
  }

  private static Optional<Correction> checkIfAdjustmentAlreadyExists(String adjustmentId,
                                                                     List<Correction> corrections,
                                                                     CorrectionTypes type) {
    return corrections.stream()
        .filter(it -> Objects.equals(it.id(), adjustmentId) && Objects.equals(it.type(), type))
        .findFirst();
  }

  private static ArrayList<Correction> getListWithoutOldAdjustment(List<Correction> corrections,
                                                                   Correction value) {
    final var existentAdjustments = new ArrayList<>(corrections);
    existentAdjustments.remove(value);
    return existentAdjustments;
  }

  public void saveAdjustment(Correction correction) {
    deliveryAdapter.findById(correction.deliveryId()).ifPresentOrElse(
        deliveryOnDb -> updateDelivery(correction, deliveryOnDb),
        () -> deliveryAdapter.save(new Delivery(correction.deliveryId(), List.of(correction)))
    );
  }

  private void updateDelivery(Correction correction, Delivery deliveryOnDb) {
    final var existentAdjustment =
        checkIfAdjustmentAlreadyExists(correction.id(), deliveryOnDb.corrections(),
            correction.type());

    ArrayList<Correction> corrections =
        existentAdjustment.map(
                value -> getListWithoutOldAdjustment(deliveryOnDb.corrections(), value))
            .orElse(new ArrayList<>(deliveryOnDb.corrections()));
    corrections.add(correction);

    deliveryAdapter.save(new Delivery(deliveryOnDb, corrections));
  }
}
