package com.weir.quarkus.eventuate.tram.sagas.customer.web;

import com.weir.quarkus.eventuate.tram.sagas.common.Money;

public class CreateCustomerRequest {
  private String name;
  private Money creditLimit;

  public CreateCustomerRequest() {
  }

  public CreateCustomerRequest(String name, Money creditLimit) {

    this.name = name;
    this.creditLimit = creditLimit;
  }


  public String getName() {
    return name;
  }

  public Money getCreditLimit() {
    return creditLimit;
  }
}
