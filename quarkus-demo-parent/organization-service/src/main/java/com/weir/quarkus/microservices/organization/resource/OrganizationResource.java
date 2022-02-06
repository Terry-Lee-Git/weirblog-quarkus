package com.weir.quarkus.microservices.organization.resource;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.weir.quarkus.microservices.organization.entity.Organization;


@Path("/org")
public class OrganizationResource {

	@GET
	@Path("/{id}")
	public Organization get(@PathParam("id") Long id) {
		return Organization.findById(id);
	}
	
	@POST
	@Transactional
	public Organization add(Organization org) {
		org.persist();
		return org;
	}
}
