package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.database.model.CorrectionDb;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CorrectionRepository extends CrudRepository<CorrectionDb, String> {
  List<CorrectionDb> findAllByDeliveryId(String deliveryId);
}
