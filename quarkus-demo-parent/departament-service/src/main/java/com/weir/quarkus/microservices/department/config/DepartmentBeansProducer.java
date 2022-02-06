package com.weir.quarkus.microservices.department.config;

import java.net.URISyntaxException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.orbitz.consul.Consul;
import com.weir.quarkus.microservices.department.client.LoadBalancedFilter;
import com.weir.quarkus.microservices.department.client.OrganizationClient;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

@ApplicationScoped
public class DepartmentBeansProducer {

	@ConfigProperty(name = "client.organization.uri")
	String organizationUri;
	@Produces
	Consul consulClient = Consul.builder().build();

	@Produces
	LoadBalancedFilter filter = new LoadBalancedFilter(consulClient);

	@Produces
	OrganizationClient organizationClient() throws URISyntaxException {
		URIBuilder builder = new URIBuilder(organizationUri);
		return RestClientBuilder.newBuilder()
				.baseUri(builder.build())
				.register(filter)
				.build(OrganizationClient.class);
	}

}
