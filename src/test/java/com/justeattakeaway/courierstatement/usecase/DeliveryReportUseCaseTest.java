package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionFactory;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
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

  final String courierId = "courierId";

  @Test
  public void shouldGetTheReportForTwoDeliveries() {
    // given
    final var delivery = DeliveryFactory.createDelivery("delivery1", 12.0);
    final var delivery2 = DeliveryFactory.createDelivery("delivery2", 13.0);
    final var adjustment = CorrectionFactory.createAdjustment("adjustment", 5.0, delivery);
    final var bonus = CorrectionFactory.createBonus("bonus", 8.0, delivery);

    final var to = LocalDate.of(2021, 1, 1);
    final var from = LocalDate.of(2021, 1, 31);
    Mockito.when(
            deliveryAdapter.findAllByCourierIdAndPeriod(courierId, to, from, Pageable.unpaged()))
        .thenReturn(new PageImpl<>(List.of(delivery, delivery2)));
    Mockito.when(correctionAdapter.findAllByDeliveryId(delivery.deliveryId()))
        .thenReturn(List.of(adjustment, bonus));
    Mockito.when(correctionAdapter.findAllByDeliveryId(delivery2.deliveryId()))
        .thenReturn(Collections.emptyList());

    // when
    final var actual = target.findByCourierAndPeriod(courierId, to, from, Pageable.unpaged());

    // then
    Assertions.assertThat(actual).containsExactlyInAnyOrder(
        deliveryWithAdjustments(delivery, adjustment, bonus),
        justDelivery(delivery2)
    );
  }

  private DeliveryReport deliveryWithAdjustments(
      Delivery delivery,
      Correction adjustment,
      Correction bonus) {
    return new DeliveryReport(delivery.deliveryId(), courierId, delivery.createdTimestamp(),
        delivery.value().add(adjustment.value()).add(bonus.value()), delivery.value(),
        getTransactionDetails(adjustment, bonus));
  }

  private DeliveryReport.Transactions getTransactionDetails(
      Correction adjustment,
      Correction bonus) {
    return new DeliveryReport.Transactions(getAdjustments(adjustment), getBonuses(bonus));
  }

  private List<DeliveryReport.Corrections> getAdjustments(Correction adjustment) {
    return Collections.singletonList(
        new DeliveryReport.Corrections(adjustment.id(), adjustment.modifiedTimestamp(),
            adjustment.value()));
  }

  private List<DeliveryReport.Corrections> getBonuses(Correction bonus) {
    return Collections.singletonList(
        new DeliveryReport.Corrections(bonus.id(), bonus.modifiedTimestamp(),
            bonus.value()));
  }

  private DeliveryReport justDelivery(Delivery delivery) {
    return new DeliveryReport(delivery.deliveryId(), courierId, delivery.createdTimestamp(),
        delivery.value(),
        delivery.value(),
        new DeliveryReport.Transactions(Collections.emptyList(), Collections.emptyList()));
  }
}
