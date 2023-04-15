package com.justeattakeaway.courierstatement.usecase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.justeattakeaway.courierstatement.adapter.SaveDeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionFactory;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionTypes;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CorrectionModifiedUseCaseTest {

  @InjectMocks
  private AdjustmentModifiedUseCase target;

  @Mock
  private SaveDeliveryAdapter saveDeliveryAdapter;

  @Test
  public void ifCantFindDeliveryInDatabaseCreateANewOneWithAdjustment() {
    // given
    final var adjustment = CorrectionFactory.createAdjustment();
    when(saveDeliveryAdapter.findById(adjustment.deliveryId())).thenReturn(Optional.empty());

    // when
    target.saveAdjustment(adjustment);

    // then
    final var expectedDelivery = new Delivery(adjustment.deliveryId(), List.of(adjustment));
    verify(saveDeliveryAdapter).save(expectedDelivery);
  }

  @Test
  public void ifFindDeliveryInDatabaseWithoutAdjustmentAddToList() {
    // given
    final var adjustment = CorrectionFactory.createAdjustment();
    final var deliveryOnDb = DeliveryFactory.createDelivery();
    when(saveDeliveryAdapter.findById(adjustment.deliveryId())).thenReturn(
        Optional.of(deliveryOnDb));

    // when
    target.saveAdjustment(adjustment);

    // then
    final var expectedDelivery = new Delivery(deliveryOnDb, List.of(adjustment));
    verify(saveDeliveryAdapter).save(expectedDelivery);
  }

  @Test
  public void ifFindDeliveryInDatabaseWithOtherTypeOfCorrectionAddToList() {
    // given
    final var adjustment = CorrectionFactory.createAdjustment();
    final var deliveryOnDb = DeliveryFactory.createDelivery();
    final var bonus = CorrectionFactory.createBonus();
    deliveryOnDb.corrections().add(bonus);
    when(saveDeliveryAdapter.findById(adjustment.deliveryId())).thenReturn(
        Optional.of(deliveryOnDb));

    // when
    target.saveAdjustment(adjustment);

    // then
    final var expectedDelivery = new Delivery(deliveryOnDb, List.of(bonus, adjustment));
    verify(saveDeliveryAdapter).save(expectedDelivery);
  }

  @Test
  public void ifFindDeliveryInDatabaseWithAdjustmentOverride() {
    // given
    final var adjustment = CorrectionFactory.createAdjustment();

    final var deliveryOnDb = DeliveryFactory.createDelivery();
    final var adjustmentOnDb =
        new Correction(adjustment.deliveryId(), adjustment.id(), CorrectionTypes.ADJUSTMENT,
            LocalDateTime.of(2018, 7, 5, 10, 0), BigDecimal.valueOf(8));
    deliveryOnDb.corrections().add(adjustmentOnDb);

    when(saveDeliveryAdapter.findById(adjustment.deliveryId())).thenReturn(Optional.of(deliveryOnDb));

    // when
    target.saveAdjustment(adjustment);

    // then
    deliveryOnDb.corrections().remove(adjustmentOnDb);
    deliveryOnDb.corrections().add(adjustment);
    verify(saveDeliveryAdapter).save(deliveryOnDb);
  }
}
