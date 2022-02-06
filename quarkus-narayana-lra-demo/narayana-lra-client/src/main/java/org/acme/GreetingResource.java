package org.acme;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.LRAStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@Path("/client")
public class GreetingResource {

	@Path("/book")
	@GET
    @LRA(value = LRA.Type.REQUIRED, end = false) // Step 2b: The method should run within an LRA
    public Response hello(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId /* Step 2c the context is useful for associating compensation logic */) {
        System.out.printf("client hello with context %s%n", lraId);
        String ticket = "1234";
//        double ss = 1/0;
        return Response.ok(ticket).build();
    }

    // Step 2d: There must be a method to compensate for the action if it's cancelled
    @PUT
    @Path("compensate")
    @Compensate
    public Response compensateWork(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        System.out.printf("client compensating %s%n", lraId);
        return Response.ok(lraId.toASCIIString()).build();
    }

    // Step 2e: An optional callback notifying that the LRA is closing
    @PUT
    @Path("complete")
    @Complete
    public Response completeWork(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        System.out.printf("client completing %s%n", lraId);
        return Response.ok(lraId.toASCIIString()).build();
    }
    
}