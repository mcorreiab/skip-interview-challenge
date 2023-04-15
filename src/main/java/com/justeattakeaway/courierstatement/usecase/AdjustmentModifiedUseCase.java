package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.SaveDeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Adjustment;
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

  private static Optional<Adjustment> checkIfAdjustmentAlreadyExists(String adjustmentId,
                                                                     List<Adjustment> adjustments) {
    return adjustments.stream().filter(it -> Objects.equals(it.adjustmentId(), adjustmentId))
        .findFirst();
  }

  private static ArrayList<Adjustment> getListWithoutOldAdjustment(List<Adjustment> adjustments,
                                                                   Adjustment value) {
    final var existentAdjustments = new ArrayList<>(adjustments);
    existentAdjustments.remove(value);
    return existentAdjustments;
  }

  public void saveAdjustment(Adjustment adjustment) {
    saveDeliveryAdapter.findById(adjustment.deliveryId()).ifPresentOrElse(
        deliveryOnDb -> updateDelivery(adjustment, deliveryOnDb),
        () -> saveDeliveryAdapter.save(new Delivery(adjustment.deliveryId(), List.of(adjustment)))
    );
  }

  private void updateDelivery(Adjustment adjustment, Delivery deliveryOnDb) {
    final var existentAdjustment =
        checkIfAdjustmentAlreadyExists(adjustment.adjustmentId(), deliveryOnDb.adjustments());

    ArrayList<Adjustment> adjustments =
        existentAdjustment.map(
                value -> getListWithoutOldAdjustment(deliveryOnDb.adjustments(), value))
            .orElse(new ArrayList<>(deliveryOnDb.adjustments()));
    adjustments.add(adjustment);

    saveDeliveryAdapter.save(new Delivery(deliveryOnDb, adjustments));
  }
}
