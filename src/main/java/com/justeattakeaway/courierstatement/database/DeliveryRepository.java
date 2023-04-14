package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.database.model.Delivery;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryRepository extends CrudRepository<Delivery, String> {
}
