package com.weir.quarkus.eventuate.tram.sagas.orders.messaging;


public class CreateOrderSagaData  {

  private Long orderId;

  private OrderDetails orderDetails;

  public CreateOrderSagaData() {
  }

  public CreateOrderSagaData(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public Long getOrderId() {
    return orderId;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
