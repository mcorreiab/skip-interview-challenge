package com.justeattakeaway.courierstatement.adapter;

import com.justeattakeaway.courierstatement.usecase.model.Correction;
import java.time.LocalDate;
import java.util.List;

public interface CorrectionAdapter {
  void save(Correction correction);
  List<Correction> findAllByDeliveryId(String deliveryId);

  List<Correction> findAllByCourierIdAndPeriod(String courierId, LocalDate from, LocalDate to);
}
