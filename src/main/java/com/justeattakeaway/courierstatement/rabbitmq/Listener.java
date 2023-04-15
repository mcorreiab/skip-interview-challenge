package com.justeattakeaway.courierstatement.rabbitmq;

import com.justeattakeaway.courierstatement.rabbitmq.model.AdjustmentModified;
import com.justeattakeaway.courierstatement.rabbitmq.model.BonusModified;
import com.justeattakeaway.courierstatement.rabbitmq.model.DeliveryCreated;
import com.justeattakeaway.courierstatement.usecase.AdjustmentModifiedUseCase;
import com.justeattakeaway.courierstatement.usecase.SaveDeliveryUseCase;
import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionTypes;
import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Listener {

  private final SaveDeliveryUseCase saveDeliveryUseCase;
  private final AdjustmentModifiedUseCase adjustmentModifiedUseCase;

  public Listener(SaveDeliveryUseCase saveDeliveryUseCase, AdjustmentModifiedUseCase adjustmentModifiedUseCase) {
    this.saveDeliveryUseCase = saveDeliveryUseCase;
    this.adjustmentModifiedUseCase = adjustmentModifiedUseCase;
  }

  @RabbitListener(queues = "${rabbitmq.queues.deliveryCreated}")
  public void processDeliveryCreated(@Valid @Payload DeliveryCreated deliveryCreated) {
    final var delivery = new Delivery(
        deliveryCreated.deliveryId(),
        deliveryCreated.courierId(),
        deliveryCreated.createdTimestamp(),
        deliveryCreated.value());
    saveDeliveryUseCase.saveDelivery(delivery);
  }

  @RabbitListener(queues = "${rabbitmq.queues.adjustmentModified}")
  public void processAdjustmentModified(@Valid @Payload AdjustmentModified adjustmentModified) {
    final var adjustment = new Correction(
        adjustmentModified.deliveryId(),
        adjustmentModified.adjustmentId(),
        CorrectionTypes.ADJUSTMENT,
        adjustmentModified.modifiedTimestamp(),
        adjustmentModified.value());
    adjustmentModifiedUseCase.saveAdjustment(adjustment);
  }

  @RabbitListener(queues = "${rabbitmq.queues.bonusModified}")
  public void processBonusModified(@Valid @Payload BonusModified bonusModified) {
    final var adjustment = new Correction(
        bonusModified.deliveryId(),
        bonusModified.bonusId(),
        CorrectionTypes.BONUS,
        bonusModified.modifiedTimestamp(),
        bonusModified.value());
    adjustmentModifiedUseCase.saveAdjustment(adjustment);
  }
}
