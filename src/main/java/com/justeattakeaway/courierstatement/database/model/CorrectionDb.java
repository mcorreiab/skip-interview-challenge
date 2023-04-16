package com.justeattakeaway.courierstatement.database.model;

import com.justeattakeaway.courierstatement.usecase.model.Correction;
import com.justeattakeaway.courierstatement.usecase.model.CorrectionTypes;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "corrections")
public class CorrectionDb {
  @Id
  String id;

  String correctionId;

  @ManyToOne
  @JoinColumn(name = "deliveryId")
  DeliveryDb delivery;
  @Enumerated(EnumType.STRING)
  CorrectionTypes type;
  LocalDateTime date;
  BigDecimal value;

  public CorrectionDb() {
  }

  public CorrectionDb(Correction correction) {
    this.id = correction.id() + "-" + correction.type().name();
    this.correctionId = correction.id();
    this.delivery = new DeliveryDb(correction.delivery());
    this.type = correction.type();
    this.date = correction.modifiedTimestamp();
    this.value = correction.value();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCorrectionId() {
    return correctionId;
  }

  public void setCorrectionId(String correctionId) {
    this.correctionId = correctionId;
  }

  public DeliveryDb getDelivery() {
    return delivery;
  }

  public void setDelivery(DeliveryDb delivery) {
    this.delivery = delivery;
  }

  public CorrectionTypes getType() {
    return type;
  }

  public void setType(CorrectionTypes type) {
    this.type = type;
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
