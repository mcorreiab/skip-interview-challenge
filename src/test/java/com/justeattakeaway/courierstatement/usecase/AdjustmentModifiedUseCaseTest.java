package com.justeattakeaway.courierstatement.usecase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.justeattakeaway.courierstatement.adapter.SaveDeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Adjustment;
import com.justeattakeaway.courierstatement.usecase.model.AdjustmentFactory;
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
public class AdjustmentModifiedUseCaseTest {

  @InjectMocks
  private AdjustmentModifiedUseCase target;

  @Mock
  private SaveDeliveryAdapter saveDeliveryAdapter;

  @Test
  public void ifCantFindDeliveryInDatabaseCreateANewOneWithAdjustment() {
    // given
    final var adjustment = AdjustmentFactory.createAdjustment();
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
    final var adjustment = AdjustmentFactory.createAdjustment();
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
  public void ifFindDeliveryInDatabaseWithAdjustmentOverride() {
    // given
    final var adjustment = AdjustmentFactory.createAdjustment();

    final var deliveryOnDb = DeliveryFactory.createDelivery();
    final var adjustmentOnDb = new Adjustment(adjustment.deliveryId(), adjustment.adjustmentId(),
        LocalDateTime.of(2018, 7, 5, 10, 0), BigDecimal.valueOf(8));
    deliveryOnDb.adjustments().add(adjustmentOnDb);

    when(saveDeliveryAdapter.findById(adjustment.deliveryId())).thenReturn(
        Optional.of(deliveryOnDb));

    // when
    target.saveAdjustment(adjustment);

    // then
    deliveryOnDb.adjustments().remove(adjustmentOnDb);
    deliveryOnDb.adjustments().add(adjustment);
    verify(saveDeliveryAdapter).save(deliveryOnDb);
  }
}
