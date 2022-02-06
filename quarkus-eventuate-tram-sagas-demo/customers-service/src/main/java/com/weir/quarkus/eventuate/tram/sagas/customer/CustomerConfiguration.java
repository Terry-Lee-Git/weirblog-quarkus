package com.weir.quarkus.eventuate.tram.sagas.customer;

import io.eventuate.tram.commands.consumer.CommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;

import javax.inject.Singleton;

import com.weir.quarkus.eventuate.tram.sagas.customer.service.CustomerCommandHandler;

@Singleton
public class CustomerConfiguration {

  // TODO Exception handler for CustomerCreditLimitExceededException

  @Singleton
  public CommandDispatcher consumerCommandDispatcher(CustomerCommandHandler target,
                                                     SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {

    return sagaCommandDispatcherFactory.make("customerCommandDispatcher", target.commandHandlerDefinitions());
  }

}
