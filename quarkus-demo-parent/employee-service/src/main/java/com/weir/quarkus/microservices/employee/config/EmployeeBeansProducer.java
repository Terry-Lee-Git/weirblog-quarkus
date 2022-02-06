package com.weir.quarkus.microservices.employee.config;

import java.net.URISyntaxException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.orbitz.consul.Consul;
import com.weir.quarkus.microservices.employee.client.DepartmentClient;
import com.weir.quarkus.microservices.employee.client.LoadBalancedFilter;
import com.weir.quarkus.microservices.employee.client.OrganizationClient;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

@ApplicationScoped
public class EmployeeBeansProducer {

//	@ConfigProperty(name = "client.organization.uri")
//	String organizationUri;
//	@ConfigProperty(name = "client.department.uri")
//	String departmentUri;
	@Produces
	Consul consulClient = Consul.builder().build();

//	@Produces
//	LoadBalancedFilter filter = new LoadBalancedFilter(consulClient);

//	@Produces
//	OrganizationClient organizationClient() throws URISyntaxException {
//		URIBuilder builder = new URIBuilder(organizationUri);
//		return RestClientBuilder.newBuilder()
//				.baseUri(builder.build())
//				.register(filter)
//				.build(OrganizationClient.class);
//	}
//	@Produces
//	DepartmentClient departmentClient() throws URISyntaxException {
//		URIBuilder builder = new URIBuilder(departmentUri);
//		return RestClientBuilder.newBuilder()
//				.baseUri(builder.build())
//				.register(filter)
//				.build(DepartmentClient.class);
//	}
	
	@Produces
	LoadBalancedFilter organizationFilter = new LoadBalancedFilter(consulClient);

	@Produces
	LoadBalancedFilter departmentFilter = new LoadBalancedFilter(consulClient);

	@Produces
	public OrganizationClient employeeClient() throws URISyntaxException {
		URIBuilder builder = new URIBuilder("http://organization");
		return RestClientBuilder.newBuilder()
				.baseUri(builder.build())
				.register(organizationFilter)
				.build(OrganizationClient.class);
	}

	@Produces
	public DepartmentClient departmentClient() throws URISyntaxException {
		URIBuilder builder = new URIBuilder("http://department");
		return RestClientBuilder.newBuilder()
				.baseUri(builder.build())
				.register(departmentFilter)
				.build(DepartmentClient.class);
	}

}
