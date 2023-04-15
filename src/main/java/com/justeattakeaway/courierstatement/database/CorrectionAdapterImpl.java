package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.database.model.CorrectionDb;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
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
        .stream().map(correction -> new Correction(
            correction.getCorrectionId(),
            new Delivery(correction.getDelivery().getId(), correction.getDelivery().getCourierId(),
                correction.getDelivery().getDate(), correction.getDelivery().getValue()),
            correction.getType(),
            correction.getDate(),
            correction.getValue())).toList();
  }
}
