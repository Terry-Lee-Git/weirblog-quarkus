package com.weir.quarkus.eventuate.tram.sagas.customer.web;


import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.weir.quarkus.eventuate.tram.sagas.customer.entity.Customer;
import com.weir.quarkus.eventuate.tram.sagas.customer.service.CustomerService;

@Path(value = "/customers")
public class CustomerController {

  @Inject
  CustomerService customerService;

  @POST
  public CreateCustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest) {
    Customer customer = customerService.createCustomer(createCustomerRequest.getName(), createCustomerRequest.getCreditLimit());
    return new CreateCustomerResponse(customer.getId());
  }
}
