package com.justeattakeaway.courierstatement.queue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DeliveryCreated(String deliveryId,
                              String courierId,
                              LocalDateTime createdTimestamp,
                              BigDecimal value) {
}
