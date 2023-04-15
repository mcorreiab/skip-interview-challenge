package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import com.justeattakeaway.courierstatement.usecase.model.WeeklyStatement;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import org.springframework.stereotype.Service;

@Service
public class StatementUseCase {
  private final DeliveryAdapter deliveryAdapter;
  private final CorrectionAdapter correctionAdapter;

  public StatementUseCase(DeliveryAdapter deliveryAdapter, CorrectionAdapter correctionAdapter) {
    this.deliveryAdapter = deliveryAdapter;
    this.correctionAdapter = correctionAdapter;
  }

  public WeeklyStatement getWeeklyStatementByCourierAndDate(String courierId, LocalDate date) {
    final var startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    final var endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

    final var deliveries =
        deliveryAdapter.findAllByCourierIdAndPeriod(courierId, startOfWeek, endOfWeek).stream()
            .map(Delivery::value).reduce(BigDecimal.ZERO, BigDecimal::add);
    final var corrections =
        correctionAdapter.findAllByCourierIdAndPeriod(courierId, startOfWeek, endOfWeek).stream()
            .map(Correction::value).reduce(BigDecimal.ZERO, BigDecimal::add);

    return new WeeklyStatement(courierId, startOfWeek, endOfWeek, deliveries.add(corrections));
  }
}
