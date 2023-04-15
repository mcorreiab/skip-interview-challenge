package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.database.model.DeliveryDb;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryRepository extends CrudRepository<DeliveryDb, String> {
}
