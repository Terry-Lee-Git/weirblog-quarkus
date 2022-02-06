package com.weir.quarkus.microservices.organization.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.weir.quarkus.microservices.organization.model.Department;
import com.weir.quarkus.microservices.organization.model.Employee;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Organization extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @Transient
    private List<Department> departments = new ArrayList<>();
    @Transient
    private List<Employee> employees = new ArrayList<>();
}
