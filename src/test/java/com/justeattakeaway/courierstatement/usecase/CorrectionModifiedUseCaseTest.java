package com.justeattakeaway.courierstatement.usecase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionFactory;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryFactory;
import java.util.Optional;
import org.assertj.core.api.Assertions;
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
  private DeliveryAdapter deliveryAdapter;

  @Mock
  private CorrectionAdapter correctionAdapter;

  @Test
  public void ifCantFindDeliveryInDatabaseReturnAnError() {
    // given
    final var adjustment = CorrectionFactory.createAdjustment();
    when(deliveryAdapter.findById(adjustment.delivery().deliveryId())).thenReturn(Optional.empty());

    // when
    Assertions.assertThatThrownBy(() -> target.saveAdjustment(adjustment))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void ifFindDeliveryInDatabaseLinkToCorrection() {
    // given
    final var adjustment = CorrectionFactory.createAdjustment();
    final var deliveryOnDb = DeliveryFactory.createDelivery();
    when(deliveryAdapter.findById(adjustment.delivery().deliveryId()))
        .thenReturn(Optional.of(deliveryOnDb));

    // when
    target.saveAdjustment(adjustment);

    // then
    verify(correctionAdapter).save(adjustment.withDelivery(deliveryOnDb));
  }
}
