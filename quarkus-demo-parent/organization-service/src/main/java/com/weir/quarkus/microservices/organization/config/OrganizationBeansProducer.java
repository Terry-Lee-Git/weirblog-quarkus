package com.weir.quarkus.microservices.organization.config;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.orbitz.consul.Consul;

@ApplicationScoped
public class OrganizationBeansProducer {

	@Produces
	Consul consulClient = Consul.builder().build();

}