package com.justeattakeaway.courierstatement.usecase;

import com.justeattakeaway.courierstatement.adapter.CorrectionAdapter;
import com.justeattakeaway.courierstatement.adapter.DeliveryAdapter;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionTypes;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import com.justeattakeaway.courierstatement.usecase.model.DeliveryReport;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReportUseCase {

  private final DeliveryAdapter deliveryAdapter;
  private final CorrectionAdapter correctionAdapter;

  public DeliveryReportUseCase(DeliveryAdapter deliveryAdapter,
                               CorrectionAdapter correctionAdapter) {
    this.deliveryAdapter = deliveryAdapter;
    this.correctionAdapter = correctionAdapter;
  }

  public Page<DeliveryReport> findByCourierAndPeriod(
      String courierId,
      LocalDate from,
      LocalDate to,
      Pageable pageable) {
    return
        deliveryAdapter.findAllByCourierIdAndPeriod(courierId, from, to, pageable).map(
            this::getDeliveryReport);
  }

  private DeliveryReport getDeliveryReport(Delivery delivery) {
    final var corrections = correctionAdapter.findAllByDeliveryId(delivery.deliveryId());

    final var correctionDetails = getCorrectionDetails(corrections);

    return new DeliveryReport(
        delivery.deliveryId(),
        delivery.courierId(),
        delivery.createdTimestamp(),
        delivery.value().add(correctionDetails.getTotalAmount()),
        delivery.value(),
        new DeliveryReport.Transactions(correctionDetails.getAdjustments(),
            correctionDetails.getBonuses())
    );
  }

  private CorrectionSummary getCorrectionDetails(List<Correction> corrections) {
    final var correctionSummary = new CorrectionSummary();

    for (final var correction : corrections) {
      correctionSummary.addAmount(correction.value());
      if (Objects.equals(correction.type(), CorrectionTypes.BONUS)) {
        correctionSummary.addBonus(
            new DeliveryReport.Corrections(correction.id(), correction.modifiedTimestamp(),
                correction.value()));
      } else {
        correctionSummary.addAdjustment(
            new DeliveryReport.Corrections(correction.id(), correction.modifiedTimestamp(),
                correction.value()));
      }
    }
    return correctionSummary;
  }

  private static class CorrectionSummary {
    private final List<DeliveryReport.Corrections> adjustments;
    private final List<DeliveryReport.Corrections> bonuses;
    private BigDecimal totalAmount;

    public CorrectionSummary() {
      this.totalAmount = BigDecimal.ZERO;
      this.adjustments = new ArrayList<>();
      this.bonuses = new ArrayList<>();
    }

    public void addAmount(BigDecimal amount) {
      totalAmount = totalAmount.add(amount);
    }

    public void addAdjustment(DeliveryReport.Corrections correction) {
      adjustments.add(correction);
    }

    public void addBonus(DeliveryReport.Corrections bonus) {
      bonuses.add(bonus);
    }

    public BigDecimal getTotalAmount() {
      return totalAmount;
    }

    public List<DeliveryReport.Corrections> getAdjustments() {
      return adjustments;
    }

    public List<DeliveryReport.Corrections> getBonuses() {
      return bonuses;
    }
  }

}
