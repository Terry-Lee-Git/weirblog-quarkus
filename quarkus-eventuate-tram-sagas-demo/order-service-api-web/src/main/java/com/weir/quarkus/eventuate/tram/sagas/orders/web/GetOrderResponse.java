package com.weir.quarkus.eventuate.tram.sagas.orders.web;

import com.weir.quarkus.eventuate.tram.sagas.common.OrderState;

public class GetOrderResponse {
  private Long orderId;
  private OrderState orderState;

  public GetOrderResponse() {
  }

  public GetOrderResponse(Long orderId, OrderState orderState) {
    this.orderId = orderId;
    this.orderState = orderState;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public OrderState getOrderState() {
    return orderState;
  }

  public void setOrderState(OrderState orderState) {
    this.orderState = orderState;
  }
}
