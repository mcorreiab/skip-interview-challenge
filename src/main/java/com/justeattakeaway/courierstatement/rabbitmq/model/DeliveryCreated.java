package com.justeattakeaway.courierstatement.rabbitmq.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DeliveryCreated(
    @NotBlank String deliveryId,
    @NotBlank String courierId,
    @NotNull LocalDateTime createdTimestamp,
    @NotNull @Min(0) BigDecimal value) implements Serializable {
}
