package com.weir.quarkus.eventuate.tram.sagas.customer.service;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.weir.quarkus.eventuate.tram.sagas.customer.messaging.ReserveCreditCommand;


@Singleton
public class CustomerCommandHandler {

  @Inject
  CustomerService customerService;

  public CommandHandlers commandHandlerDefinitions() {
    return SagaCommandHandlersBuilder
            .fromChannel("customerService")
            .onMessage(ReserveCreditCommand.class, this::reserveCredit)
            .build();
  }

  public Message reserveCredit(CommandMessage<ReserveCreditCommand> cm) {
    ReserveCreditCommand cmd = cm.getCommand();
    return customerService.reserveCredit(cmd.getCustomerId(), cmd.getOrderId(), cmd.getOrderTotal());
  }

  // withLock(Customer.class, customerId).
  // TODO @Validate to trigger validation and error reply
}
