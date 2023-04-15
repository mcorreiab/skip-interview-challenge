package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.database.model.CorrectionDb;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CorrectionRepository extends CrudRepository<CorrectionDb, String> {
  List<CorrectionDb> findAllByDeliveryId(String deliveryId);
  List<CorrectionDb> findAllByDateBetweenAndDelivery_CourierId(LocalDateTime fromDate, LocalDateTime toDate, String courierId);
}
