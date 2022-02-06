package com.weir.quarkus.microservices.employee.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class Organization {
    private Long id;
    private String name;
    private String address;
}
