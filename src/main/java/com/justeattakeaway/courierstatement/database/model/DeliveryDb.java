package com.justeattakeaway.courierstatement.database.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deliveries")
public record DeliveryDb(
    @Id String deliveryId,
    String courierId,
    LocalDateTime createdTimestamp,
    BigDecimal value,
    List<CorrectionDb> adjustments
) {
}
