package com.justeattakeaway.courierstatement.database.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deliveries")
public record Delivery(@Id String deliveryId, String courierId, LocalDateTime createdTimestamp, BigDecimal value) {
}
