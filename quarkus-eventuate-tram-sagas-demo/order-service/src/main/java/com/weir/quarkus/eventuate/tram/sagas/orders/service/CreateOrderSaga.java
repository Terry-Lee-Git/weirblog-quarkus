package com.weir.quarkus.eventuate.tram.sagas.orders.service;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weir.quarkus.eventuate.tram.sagas.common.Money;
import com.weir.quarkus.eventuate.tram.sagas.orders.messaging.CreateOrderSagaData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreateOrderSaga implements SimpleSaga<CreateOrderSagaData> {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Inject
  OrderService orderService;

  private SagaDefinition<CreateOrderSagaData> sagaDefinition =
          step()
            .invokeLocal(this::create)
            .withCompensation(this::reject)
          .step()
            .invokeParticipant(this::reserveCredit)
          .step()
            .invokeLocal(this::approve)
          .build();

  @Override
  public SagaDefinition<CreateOrderSagaData> getSagaDefinition() {
    return this.sagaDefinition;
  }

  private void create(CreateOrderSagaData data) {
    orderService.createOrder(data);
  }

  private CommandWithDestination reserveCredit(CreateOrderSagaData data) {
    long orderId = data.getOrderId();
    Long customerId = data.getOrderDetails().getCustomerId();
    Money orderTotal = data.getOrderDetails().getOrderTotal();
    return orderService.reserveCredit(orderId, customerId, orderTotal);
  }

  private void approve(CreateOrderSagaData data) {
    orderService.approveOrder(data.getOrderId());
  }

  public void reject(CreateOrderSagaData data) {
    orderService.rejectOrder(data.getOrderId());
  }
}
