package com.justeattakeaway.courierstatement.controller;

import com.justeattakeaway.courierstatement.usecase.DeliveryReportUseCase;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryReport;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CourierReportController {

  final DeliveryReportUseCase deliveryReportUseCase;

  public CourierReportController(DeliveryReportUseCase deliveryReportUseCase) {
    this.deliveryReportUseCase = deliveryReportUseCase;
  }

  @GetMapping(value = "/couriers/{courierId}/report/from/{from}/to/{to}")
  public ResponseEntity<Page<DeliveryReport>> getCourierReport(@PathVariable String courierId,
                                                               @PathVariable LocalDate from,
                                                               @PathVariable LocalDate to,
                                                               Pageable pageable) {
    return ResponseEntity.ok(deliveryReportUseCase.findByCourierAndPeriod(courierId, from, to, pageable));
  }
}
