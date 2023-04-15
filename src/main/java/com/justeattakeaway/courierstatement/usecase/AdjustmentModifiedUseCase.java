package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.SaveDeliveryAdapter;
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
  private final SaveDeliveryAdapter saveDeliveryAdapter;

  public AdjustmentModifiedUseCase(SaveDeliveryAdapter saveDeliveryAdapter) {
    this.saveDeliveryAdapter = saveDeliveryAdapter;
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
    saveDeliveryAdapter.findById(correction.deliveryId()).ifPresentOrElse(
        deliveryOnDb -> updateDelivery(correction, deliveryOnDb),
        () -> saveDeliveryAdapter.save(new Delivery(correction.deliveryId(), List.of(correction)))
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

    saveDeliveryAdapter.save(new Delivery(deliveryOnDb, corrections));
  }
}
