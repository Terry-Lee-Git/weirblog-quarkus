package com.weir.quarkus.microservices.department.resource;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.weir.quarkus.microservices.department.client.OrganizationClient;
import com.weir.quarkus.microservices.department.entity.Department;
import com.weir.quarkus.microservices.department.model.Organization;


@Path("department")
public class DepartmentResource {
	
	@Inject
	private OrganizationClient organizationClient;

	@GET
	@Path("/{id}")
	public Department get(@PathParam("id") Long id) {
		return Department.findById(id);
	}
	@GET
	@Path("/with-org/{id}")
	public Department getWithOrg(@PathParam("id") Long id) {
		Department department = Department.findById(id);
		Organization organization = organizationClient.get(department.getOrganizationId());
		department.setOrganization(organization);
		return department;
	}
	
	@POST
	@Transactional
	public Department add(Department department) {
		department.persist();
		return department;
	}
}
