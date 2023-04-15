package com.justeattakeaway.courierstatement.database.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdjustmentDb(
    String adjustmentId,
    LocalDateTime modifiedTimestamp,
    BigDecimal value
) {
}
