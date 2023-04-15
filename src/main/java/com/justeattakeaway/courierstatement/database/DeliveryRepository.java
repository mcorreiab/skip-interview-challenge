package com.justeattakeaway.courierstatement.database;

import com.justeattakeaway.courierstatement.database.model.DeliveryDb;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeliveryRepository extends CrudRepository<DeliveryDb, String>,
    PagingAndSortingRepository<DeliveryDb, String> {
  Page<DeliveryDb> findAllByDateBetweenAndCourierId(LocalDateTime fromDate,
                                                    LocalDateTime toDate,
                                                    String courierId,
                                                    Pageable pageable);
}
