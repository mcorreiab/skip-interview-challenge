package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Adjustment(
    String deliveryId,
    String adjustmentId,
    LocalDateTime modifiedTimestamp,
    BigDecimal value) {
}
