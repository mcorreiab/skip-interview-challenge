package com.justeattakeaway.courierstatement.adapter;

import com.justeattakeaway.courierstatement.rabbitmq.DeliveryCreated;

public interface SaveDeliveryAdapter {
   void save(DeliveryCreated deliveryCreated);
}
