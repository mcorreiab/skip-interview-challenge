package com.justeattakeaway.courierstatement.usecase;

import static org.mockito.Mockito.verify;

import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryFactory;
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
  private DeliveryAdapter deliveryAdapter;

  @Test
  public void shouldSaveDeliveryOnDatabaseWithoutChangingCorrection() {
    // given
    final var delivery = DeliveryFactory.createDelivery();

    // when
    target.saveDelivery(delivery);

    // then
    verify(deliveryAdapter).save(delivery);
  }
}
