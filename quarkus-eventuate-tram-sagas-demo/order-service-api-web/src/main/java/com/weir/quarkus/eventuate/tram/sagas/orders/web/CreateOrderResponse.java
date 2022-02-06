package com.weir.quarkus.eventuate.tram.sagas.orders.web;


public class CreateOrderResponse {
  private Long orderId;

  public CreateOrderResponse() {
  }

  public CreateOrderResponse(Long orderId) {
    this.orderId = orderId;
  }

  public Long getOrderId() {
    return orderId;
  }
}
