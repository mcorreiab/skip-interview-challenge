package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.database.model.DeliveryDb;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class DeliveryAdapterImpl implements DeliveryAdapter {
  private final DeliveryRepository deliveryRepository;

  public DeliveryAdapterImpl(DeliveryRepository deliveryRepository) {
    this.deliveryRepository = deliveryRepository;
  }

  public void save(Delivery delivery) {
    deliveryRepository.save(new DeliveryDb(delivery));
  }

  @Override
  public Optional<Delivery> findById(String deliveryId) {
    return deliveryRepository.findById(deliveryId)
        .map(this::mapDbToEntity);
  }

  @Override
  public Page<Delivery> findAllByCourierIdAndPeriod(String courierId, LocalDate from, LocalDate to,
                                                    Pageable pageable) {
    return deliveryRepository.findAllByDateBetweenAndCourierId(from.atStartOfDay(),
            atEndOfDay(to), courierId, pageable)
        .map(this::mapDbToEntity);
  }

  @Override
  public List<Delivery> findAllByCourierIdAndPeriod(String courierId, LocalDate from,
                                                    LocalDate to) {
    return deliveryRepository.findAllByDateBetweenAndCourierId(from.atStartOfDay(),
            atEndOfDay(to), courierId)
        .stream()
        .map(this::mapDbToEntity)
        .toList();
  }

  private static LocalDateTime atEndOfDay(LocalDate date) {
    return date.atTime(23, 59, 59, 999999999);
  }

  private Delivery mapDbToEntity(DeliveryDb delivery) {
    return new Delivery(
        delivery.getId(),
        delivery.getCourierId(),
        delivery.getDate(),
        delivery.getValue()
    );
  }
}
