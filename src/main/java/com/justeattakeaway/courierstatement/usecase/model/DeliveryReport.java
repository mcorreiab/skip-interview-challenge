package com.justeattakeaway.courierstatement.usecase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DeliveryReport(
    String deliveryId,
    String courierId,
    LocalDateTime date,
    BigDecimal totalAmount,
    BigDecimal deliveryAmount,
    Transactions transactions) {

  public record Transactions(List<Corrections> adjustments, List<Corrections> bonuses) {}

  public record Corrections(String id, LocalDateTime date, BigDecimal amount) {}
}