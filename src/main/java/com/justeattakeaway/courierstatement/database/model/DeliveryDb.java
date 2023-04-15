package com.justeattakeaway.courierstatement.database.model;

import com.justeattakeaway.courierstatement.usecase.model.Delivery;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class DeliveryDb {
  @Id
  String id;
  String courierId;
  LocalDateTime date;
  BigDecimal value;

  public DeliveryDb() {
  }

  public DeliveryDb(Delivery delivery) {
    this.id = delivery.deliveryId();
    this.courierId = delivery.courierId();
    this.date = delivery.createdTimestamp();
    this.value = delivery.value();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCourierId() {
    return courierId;
  }

  public void setCourierId(String courierId) {
    this.courierId = courierId;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }
}
