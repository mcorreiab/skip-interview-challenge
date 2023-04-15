package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionFactory;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryFactory;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryReport;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class DeliveryReportUseCaseTest {
  @InjectMocks
  private DeliveryReportUseCase target;

  @Mock
  private DeliveryAdapter deliveryAdapter;

  @Mock
  private CorrectionAdapter correctionAdapter;

  @Test
  public void shouldGetTheReportForTwoDeliveries() {
    // given
    final var courierId = "courierId";
    final var delivery = DeliveryFactory.createDelivery("delivery1", 12.0);
    final var delivery2 = DeliveryFactory.createDelivery("delivery2", 13.0);
    final var correction = CorrectionFactory.createAdjustment("delivery1", 5.0, delivery);
    final var correction2 = CorrectionFactory.createAdjustment("delivery2", 8.0, delivery);

    final var to = LocalDate.of(2021, 1, 1);
    final var from = LocalDate.of(2021, 1, 31);
    Mockito.when(
            deliveryAdapter.findAllByCourierIdAndPeriod(courierId, to, from, Pageable.unpaged()))
        .thenReturn(new PageImpl<>(List.of(delivery, delivery2)));
    Mockito.when(correctionAdapter.findAllByDeliveryId(delivery.deliveryId()))
        .thenReturn(List.of(correction, correction2));
    Mockito.when(correctionAdapter.findAllByDeliveryId(delivery2.deliveryId()))
        .thenReturn(Collections.emptyList());

    // when
    final var actual = target.findByCourierAndPeriod(courierId, to, from, Pageable.unpaged());

    // then
    Assertions.assertThat(actual).containsExactlyInAnyOrder(
        new DeliveryReport(delivery.deliveryId(), courierId, delivery.createdTimestamp(),
            delivery.value().add(correction.value()).add(correction2.value())),
        new DeliveryReport(delivery2.deliveryId(), courierId, delivery2.createdTimestamp(),
            delivery2.value())
    );
  }

}
