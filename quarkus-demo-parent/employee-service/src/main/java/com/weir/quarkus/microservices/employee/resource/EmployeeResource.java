package com.weir.quarkus.microservices.employee.resource;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.weir.quarkus.microservices.employee.client.DepartmentClient;
import com.weir.quarkus.microservices.employee.client.OrganizationClient;
import com.weir.quarkus.microservices.employee.entity.Employee;
import com.weir.quarkus.microservices.employee.model.Department;
import com.weir.quarkus.microservices.employee.model.Organization;
import com.weir.quarkus.microservices.employee.repository.EmployeeRepository;

@Path("/employees")
public class EmployeeResource {

	@Inject
	EmployeeRepository employeeRepository;
	
	@Inject
	private OrganizationClient organizationClient;
	
	@Inject
	private DepartmentClient departmentClient;
	
	@GET
	@Path("/with-department-org/{id}")
	public Employee getWithDepOrg(@PathParam("id") Long id) {
		Employee employee = employeeRepository.findById(id);
		System.out.println("----------------------employee-----" + employee.toString());
		Department department = departmentClient.get(employee.getDepartmentId());
		Organization organization = organizationClient.get(employee.getOrganizationId());
		employee.setDepartment(department);
		employee.setOrganization(organization);
		return employee;
	}
	@GET
	@Path("/{id}")
	public Employee get(@PathParam("id") Long id) {
		return employeeRepository.findById(id);
	}
	
	@POST
	@Transactional
	public Employee add(Employee employee) {
		employeeRepository.persist(employee);
		return employee;
	}
}
