package com.weir.quarkus.microservices.employee.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.weir.quarkus.microservices.employee.model.Department;
import com.weir.quarkus.microservices.employee.model.Organization;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Employee {

	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	private Long organizationId;
	@NotNull
	private Long departmentId;
	@NotBlank
	private String name;
	@Min(1)
	@Max(100)
	private int age;
	@NotBlank
	private String position;
	
	@Transient
	private Department department;
	@Transient
	private Organization organization;

}
