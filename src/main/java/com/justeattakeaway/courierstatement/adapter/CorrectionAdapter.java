package com.justeattakeaway.courierstatement.adapter;

import com.justeattakeaway.courierstatement.usecase.model.Correction;
import java.util.List;

public interface CorrectionAdapter {
  void save(Correction correction);
  List<Correction> findByDeliveryId(String deliveryId);
}
