package com.justeattakeaway.courierstatement.adapter;

import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryAdapter {
  void save(Delivery delivery);

  Optional<Delivery> findById(String deliveryId);

  Page<Delivery> findAllByCourierIdAndPeriod(String courierId, LocalDate from, LocalDate to,
                                                    Pageable pageable);
  List<Delivery> findAllByCourierIdAndPeriod(String courierId, LocalDate from, LocalDate to);
}
