package com.weir.quarkus.eventuate.tram.sagas.customer.service;

import io.eventuate.tram.messaging.common.Message;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.weir.quarkus.eventuate.tram.sagas.common.Money;
import com.weir.quarkus.eventuate.tram.sagas.customer.entity.Customer;
import com.weir.quarkus.eventuate.tram.sagas.customer.entity.CustomerCreditLimitExceededException;
import com.weir.quarkus.eventuate.tram.sagas.customer.messaging.CustomerCreditReservationFailed;
import com.weir.quarkus.eventuate.tram.sagas.customer.messaging.CustomerCreditReserved;

import java.util.Optional;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

@Singleton
public class CustomerService {

  @PersistenceContext
  EntityManager entityManager;

  @Transactional
  public Customer createCustomer(String name, Money creditLimit) {
    Customer customer  = new Customer(name, creditLimit);
    entityManager.persist(customer);
    return customer;
  }

  public Message reserveCredit(long customerId, Long orderId, Money orderTotal) {
    Optional<Customer> customer = Optional.ofNullable(entityManager.find(Customer.class, customerId));
    try {
      customer
              .orElseThrow(() -> new IllegalArgumentException(String.format("customer with id %s is not found", customerId)))
              .reserveCredit(orderId, orderTotal);
      return withSuccess(new CustomerCreditReserved());
    } catch (CustomerCreditLimitExceededException e) {
      return withFailure(new CustomerCreditReservationFailed());
    }
  }
}
