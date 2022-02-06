package com.weir.quarkus.eventuate.tram.sagas.orders.service;

import io.eventuate.tram.sagas.orchestration.SagaManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.weir.quarkus.eventuate.tram.sagas.orders.entity.Order;
import com.weir.quarkus.eventuate.tram.sagas.orders.messaging.CreateOrderSagaData;
import com.weir.quarkus.eventuate.tram.sagas.orders.messaging.OrderDetails;

@Singleton
public class OrderSagaService {

  @Inject
  SagaManager<CreateOrderSagaData> createOrderSagaManager;

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public Order createOrder(OrderDetails orderDetails) {
    CreateOrderSagaData data = new CreateOrderSagaData(orderDetails);
    createOrderSagaManager.create(data);
    return entityManager.find(Order.class, data.getOrderId());
  }
}
