package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryReport;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReportUseCase {

  private final DeliveryAdapter deliveryAdapter;
  private final CorrectionAdapter correctionAdapter;

  public DeliveryReportUseCase(DeliveryAdapter deliveryAdapter,
                               CorrectionAdapter correctionAdapter) {
    this.deliveryAdapter = deliveryAdapter;
    this.correctionAdapter = correctionAdapter;
  }

  public Page<DeliveryReport> findByCourierAndPeriod(String courierId,
                                                     LocalDate from,
                                                     LocalDate to,
                                                     Pageable pageable) {
    return
        deliveryAdapter.findAllByCourierIdAndPeriod(courierId, from, to, pageable).map(delivery -> {
          final var correction = correctionAdapter.findAllByDeliveryId(delivery.deliveryId());

          final var sum = delivery.value()
              .add(correction.stream().map(Correction::value)
                  .reduce(BigDecimal.ZERO, BigDecimal::add));

          return new DeliveryReport(delivery.deliveryId(), delivery.courierId(),
              delivery.createdTimestamp(), sum);
        });
  }

}
