package com.justeattakeaway.courierstatement.rabbitmq;

import com.justeattakeaway.courierstatement.rabbitmq.model.DeliveryCreated;
import com.justeattakeaway.courierstatement.usecase.SaveDeliveryUseCase;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Listener {

  private final SaveDeliveryUseCase saveDeliveryUseCase;

    public Listener(SaveDeliveryUseCase saveDeliveryUseCase) {
        this.saveDeliveryUseCase = saveDeliveryUseCase;
    }

  @RabbitListener(queues = "deliveryCreated")
  public void processDeliveryCreated(@Valid @Payload DeliveryCreated deliveryCreated) {
    saveDeliveryUseCase.saveDelivery(deliveryCreated);
  }
}
