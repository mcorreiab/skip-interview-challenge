package com.justeattakeaway.courierstatement.rabbitmq.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdjustmentModified(String adjustmentId, String deliveryId, LocalDateTime modifiedTimestamp, BigDecimal value) {
}
