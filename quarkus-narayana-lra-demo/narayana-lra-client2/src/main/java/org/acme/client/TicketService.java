package org.acme.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/client")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface TicketService {

	@GET
	@Path("/book")
	String bookTicket();
}
