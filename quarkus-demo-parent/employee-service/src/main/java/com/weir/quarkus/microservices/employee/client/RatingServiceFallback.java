package com.weir.quarkus.microservices.employee.client;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

import com.weir.quarkus.microservices.employee.model.Organization;

public class RatingServiceFallback implements FallbackHandler<Organization> {

   @Override
   public Organization handle(ExecutionContext context) {
	   Organization org = new Organization();
	   org.setId(1L);
	   org.setAddress("成都");
	   org.setName("weir");
       return org;
   }
 
}