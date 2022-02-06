package com.weir.quarkus.eventuate.tram.sagas.orders.entity;


import io.eventuate.tram.events.publisher.ResultWithEvents;

import javax.persistence.*;

import com.weir.quarkus.eventuate.tram.sagas.common.OrderState;
import com.weir.quarkus.eventuate.tram.sagas.orders.messaging.OrderDetails;

import java.util.Collections;

@Entity
@Table(name="orders")
@Access(AccessType.FIELD)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private OrderState state;

  @Embedded
  private OrderDetails orderDetails;

  @Version
  private Long version;

  public Order() {
  }

  public Order(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
    this.state = OrderState.PENDING;
  }

  public static ResultWithEvents<Order> createOrder(OrderDetails orderDetails) {
    return new ResultWithEvents<Order>(new Order(orderDetails), Collections.emptyList());
  }

  public Long getId() {
    return id;
  }

  public void noteCreditReserved() {
    this.state = OrderState.APPROVED;
  }

  public void noteCreditReservationFailed() {
    this.state = OrderState.REJECTED;
  }

  public OrderState getState() {
    return state;
  }
}
