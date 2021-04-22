package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Integer paymentId;

  @Column(name = "booking_uuid")
  private String paymentBookingUuid;

  @Column(name = "stripe_uuid")
  private String paymentStripeUuid;

  @Column(name = "status")
  private String paymentStatus;

  public Payment() {}

  public Payment(
    Integer paymentId,
    String paymentBookingUuid,
    String paymentStripeUuid,
    String paymentStatus
  ) {
    this.paymentId = paymentId;
    this.paymentBookingUuid = paymentBookingUuid;
    this.paymentStripeUuid = paymentStripeUuid;
    this.paymentStatus = paymentStatus;
  }

  public Payment(
    String paymentBookingUuid,
    String paymentStripeUuid,
    String paymentStatus
  ) {
    this.paymentBookingUuid = paymentBookingUuid;
    this.paymentStripeUuid = paymentStripeUuid;
    this.paymentStatus = paymentStatus;
  }


  public Integer getPaymentId() {
    return this.paymentId;
  }

  public void setPaymentId(Integer paymentId) {
    this.paymentId = paymentId;
  }

  public String getPaymentBookingUuid() {
    return this.paymentBookingUuid;
  }

  public void setPaymentBookingUuid(String paymentBookingUuid) {
    this.paymentBookingUuid = paymentBookingUuid;
  }

  public String getPaymentStripeUuid() {
    return this.paymentStripeUuid;
  }

  public void setPaymentStripeUuid(String paymentStripeUuid) {
    this.paymentStripeUuid = paymentStripeUuid;
  }

  public String getPaymentStatus() {
    return this.paymentStatus;
  }

  public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }
}
