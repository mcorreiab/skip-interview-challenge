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
public class SaveDeliveryUseCaseTest {

  @InjectMocks
  private SaveDeliveryUseCase target;

  @Mock
  private SaveDeliveryAdapter saveDeliveryAdapter;

  @Test
  public void ifCantFindDeliveryInDatabaseCreateANewOne() {
    // given
    final var delivery = DeliveryFactory.createDelivery();
    when(saveDeliveryAdapter.findById(delivery.deliveryId())).thenReturn(Optional.empty());

    // when
    target.saveDelivery(delivery);

    // then
    verify(saveDeliveryAdapter).save(delivery);
  }

  @Test
  public void ifFindDeliveryInDatabaseOverrideWithOwnInformation() {
    // given
    final var delivery = DeliveryFactory.createDelivery();
    final var adjustment = AdjustmentFactory.createAdjustment();

    final var deliveryOnDb = new Delivery(delivery.deliveryId(), List.of(adjustment));
    when(saveDeliveryAdapter.findById(delivery.deliveryId())).thenReturn(Optional.of(deliveryOnDb));

    // when
    target.saveDelivery(delivery);

    // then
    final var expectedDelivery =
        new Delivery(
            delivery.deliveryId(),
            delivery.courierId(),
            delivery.createdTimestamp(),
            delivery.value(),
            deliveryOnDb.adjustments()
        );
    verify(saveDeliveryAdapter).save(expectedDelivery);
  }
}
