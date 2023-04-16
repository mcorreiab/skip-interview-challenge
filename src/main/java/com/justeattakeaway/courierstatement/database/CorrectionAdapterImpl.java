package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.database.model.CorrectionDb;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CorrectionAdapterImpl implements CorrectionAdapter {

  private final CorrectionRepository correctionRepository;

  public CorrectionAdapterImpl(CorrectionRepository correctionRepository) {
    this.correctionRepository = correctionRepository;
  }

  @Override
  public void save(Correction correction) {
    correctionRepository.save(new CorrectionDb(correction));
  }

  @Override
  public List<Correction> findAllByDeliveryId(String deliveryId) {
    return correctionRepository.findAllByDeliveryId(deliveryId)
        .stream().map(this::mapDbToEntity).toList();
  }

  @Override
  public List<Correction> findAllByCourierIdAndPeriod(String courierId, LocalDate from, LocalDate to) {
    return correctionRepository.findAllByDateBetweenAndDelivery_CourierId(from.atStartOfDay(),
            atEndOfDay(to), courierId)
        .stream().map(this::mapDbToEntity).toList();
  }

  private Correction mapDbToEntity(CorrectionDb correction) {
    return new Correction(
        correction.getCorrectionId(),
        new Delivery(correction.getDelivery().getId(), correction.getDelivery().getCourierId(),
            correction.getDelivery().getDate(), correction.getDelivery().getValue()),
        correction.getType(),
        correction.getDate(),
        correction.getValue());
  }

  private LocalDateTime atEndOfDay(LocalDate date) {
    return date.atTime(23, 59, 59, 999999999);
  }
}
