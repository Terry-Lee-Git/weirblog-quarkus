package org.acme;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.client.BookService;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@Path("/client2")
public class GreetingResource {
	
	@Inject
	BookService bookService;

	@Path("/book")
	@GET
    @LRA(LRA.Type.REQUIRED) // Step 2b: The method should run within an LRA
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId /* Step 2c the context is useful for associating compensation logic */) {
        System.out.printf("client2 hello with context %s%n", lraId);
        String bookTrip = bookService.bookTrip();
        return Response.ok(bookTrip).build();
    }

    // Step 2d: There must be a method to compensate for the action if it's cancelled
    @PUT
    @Path("compensate")
    @Compensate
    public Response compensateWork(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        System.out.printf("client2 compensating %s%n", lraId);
        return Response.ok(lraId.toASCIIString()).build();
    }

    // Step 2e: An optional callback notifying that the LRA is closing
    @PUT
    @Path("complete")
    @Complete
    public Response completeWork(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
        System.out.printf("client2 completing %s%n", lraId);
        return Response.ok(lraId.toASCIIString()).build();
    }
    
//    @GET
//    @Path("/start")
//    @LRA(end = false) // Step 3a: The method should run within an LRA
//    @Produces(MediaType.TEXT_PLAIN)
//    public String start(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
//        System.out.printf("hello with context %s%n", lraId);
//        return lraId.toASCIIString();
//    }
//    
//    @GET
//    @Path("/end")
//    @LRA(value = LRA.Type.MANDATORY) // Step 3a: The method MUST be invoked with an LRA
//    @Produces(MediaType.TEXT_PLAIN)
//    public String end(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId) {
//        return lraId.toASCIIString();
//    }
    
}