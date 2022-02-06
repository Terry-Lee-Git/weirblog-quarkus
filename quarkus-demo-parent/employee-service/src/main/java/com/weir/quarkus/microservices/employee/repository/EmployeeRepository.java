package com.weir.quarkus.microservices.employee.repository;

import javax.enterprise.context.ApplicationScoped;

import com.weir.quarkus.microservices.employee.entity.Employee;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {

}
