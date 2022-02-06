package com.weir.quarkus.eventuate.tram.sagas.orders.web;

import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.eventuate.tram.sagas.orchestration.SagaManagerImpl;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.weir.quarkus.eventuate.tram.sagas.orders.entity.Order;
import com.weir.quarkus.eventuate.tram.sagas.orders.messaging.CreateOrderSagaData;
import com.weir.quarkus.eventuate.tram.sagas.orders.messaging.OrderDetails;
import com.weir.quarkus.eventuate.tram.sagas.orders.service.OrderSagaService;

import java.util.Optional;

@Path("/orders")
public class OrderController {

  @Inject
  OrderSagaService orderSagaService;

  @PersistenceContext
  EntityManager entityManager;

  @Inject
  Instance<SagaManagerImpl<CreateOrderSagaData>> sagaManagers1;

  @Inject
  Instance<SagaManager<CreateOrderSagaData>> sagaManagers2;

  @POST
  public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
    Order order = orderSagaService.createOrder(new OrderDetails(createOrderRequest.getCustomerId(), createOrderRequest.getOrderTotal()));
    return new CreateOrderResponse(order.getId());
  }

  @GET
  @Path("/{orderId}")
  @Transactional
  public GetOrderResponse getOrder(@PathParam("orderId")Long orderId) {
    return Optional.ofNullable(entityManager.find(Order.class, orderId))
            .map(order -> new GetOrderResponse(order.getId(), order.getState()))
            .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
  }
}
