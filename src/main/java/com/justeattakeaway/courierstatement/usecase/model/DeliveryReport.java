package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DeliveryReport(String deliveryId,
                             String courierId,
                             LocalDateTime date,
                             BigDecimal amount) {
}