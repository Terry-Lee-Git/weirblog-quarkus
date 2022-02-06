package com.weir.quarkus.microservices.employee.client;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.weir.quarkus.microservices.employee.model.Department;

@RegisterRestClient
@Singleton
@Path("/department")
public interface DepartmentClient {

	@GET
	@Path("/{id}")
	public Department get(@PathParam("id") Long id);
}
