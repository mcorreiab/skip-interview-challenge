package com.justeattakeaway.courierstatement.rabbitmq.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdjustmentModified(
    @NotBlank String adjustmentId,
    @NotBlank String deliveryId,
    @NotNull LocalDateTime modifiedTimestamp,
    @NotNull BigDecimal value) {
}
