package com.weir.quarkus.eventuate.tram.sagas.orders.messaging;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import com.weir.quarkus.eventuate.tram.sagas.common.Money;

@Embeddable
@Access(AccessType.FIELD)
public class OrderDetails {

  private Long customerId;

  @Embedded
  private Money orderTotal;

  public OrderDetails() {
  }

  public OrderDetails(Long customerId, Money orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public Money getOrderTotal() {
    return orderTotal;
  }
}
