package com.weir.quarkus.microservices.department.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.weir.quarkus.microservices.department.model.Organization;

@Path("/org")
public interface OrganizationClient {

	@GET
	@Path("/{id}")
	public Organization get(@PathParam("id") Long id);
}
