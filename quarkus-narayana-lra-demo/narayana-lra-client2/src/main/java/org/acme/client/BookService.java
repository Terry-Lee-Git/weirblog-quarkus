package org.acme.client;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class BookService {

	@Inject
	@RestClient
	TicketService ticketService;
	
	public String bookTrip() {
		return ticketService.bookTicket();
	}
}
