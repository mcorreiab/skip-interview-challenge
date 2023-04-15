package com.justeattakeaway.courierstatement.controller;

import com.justeattakeaway.courierstatement.usecase.DeliveryReportUseCase;
import com.justeattakeaway.courierstatement.usecase.StatementUseCase;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryReport;
import com.justeattakeaway.courierstatement.usecase.model.WeeklyStatement;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/couriers/{courierId}")
public class CourierReportController {

  final DeliveryReportUseCase deliveryReportUseCase;
  final StatementUseCase statementUseCase;

  public CourierReportController(DeliveryReportUseCase deliveryReportUseCase,
                                 StatementUseCase statementUseCase) {
    this.deliveryReportUseCase = deliveryReportUseCase;
    this.statementUseCase = statementUseCase;
  }

  @GetMapping(value = "/report/from/{from}/to/{to}")
  public ResponseEntity<Page<DeliveryReport>> getCourierReport(@PathVariable String courierId,
                                                               @PathVariable LocalDate from,
                                                               @PathVariable LocalDate to,
                                                               Pageable pageable) {
    final var reports =
        deliveryReportUseCase.findByCourierAndPeriod(courierId, from, to, pageable);

    if (reports.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(reports);
  }

  @GetMapping(value = "/statement/week/{date}")
  public ResponseEntity<WeeklyStatement> getWeeklyStatement(@PathVariable String courierId,
                                                            @PathVariable LocalDate date) {
    final var statement = statementUseCase.getWeeklyStatementByCourierAndDate(courierId, date);

    if (statement.totalAmount().equals(BigDecimal.ZERO)) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(statement);
  }
}
