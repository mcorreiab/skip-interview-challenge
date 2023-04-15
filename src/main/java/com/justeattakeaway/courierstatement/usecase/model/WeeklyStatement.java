package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WeeklyStatement(
    String courierId,
    LocalDate weekStart,
    LocalDate weekEnd,
    BigDecimal totalAmount
) {}