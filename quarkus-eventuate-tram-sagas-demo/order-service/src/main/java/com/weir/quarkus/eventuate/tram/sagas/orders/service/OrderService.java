package com.weir.quarkus.eventuate.tram.sagas.orders.service;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.events.publisher.ResultWithEvents;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.weir.quarkus.eventuate.tram.sagas.common.Money;
import com.weir.quarkus.eventuate.tram.sagas.customer.messaging.ReserveCreditCommand;
import com.weir.quarkus.eventuate.tram.sagas.orders.entity.Order;
import com.weir.quarkus.eventuate.tram.sagas.orders.messaging.CreateOrderSagaData;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

@Singleton
public class OrderService {

  @PersistenceContext
  EntityManager entityManager;

  public void createOrder(CreateOrderSagaData data) {
    ResultWithEvents<Order> oe = Order.createOrder(data.getOrderDetails());
    Order order = oe.result;
    entityManager.persist(order);
    data.setOrderId(order.getId());
  }

  public CommandWithDestination reserveCredit(long orderId, Long customerId, Money orderTotal) {
    return send(new ReserveCreditCommand(customerId, orderId, orderTotal))
            .to("customerService")
            .build();
  }

  public void approveOrder(long orderId) {
    entityManager.find(Order.class, orderId).noteCreditReserved();
  }

  public void rejectOrder(long orderId) {
    entityManager.find(Order.class, orderId).noteCreditReservationFailed();
  }

}
