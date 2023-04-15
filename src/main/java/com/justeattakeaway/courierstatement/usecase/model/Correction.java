package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Correction(
    String deliveryId,
    String id,
    CorrectionTypes type,
    LocalDateTime modifiedTimestamp,
    BigDecimal value) {
}
