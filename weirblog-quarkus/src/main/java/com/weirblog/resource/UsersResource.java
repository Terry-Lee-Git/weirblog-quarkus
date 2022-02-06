package com.weirblog.resource;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;

import com.weirblog.entity.Users;
import com.weirblog.vo.DataGrid;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("user")
@ApplicationScoped
public class UsersResource {

	@GET
	@Path("list")
	public DataGrid<Users> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		PanacheQuery<PanacheEntityBase> panacheQuery = Users.findAll().page(page-1, rows);
		DataGrid<Users> dataGrid = new DataGrid<Users>(panacheQuery.count(), panacheQuery.list());
		return dataGrid;
	}

	@GET
	@Path("{id}")
	public Users getSingle(@PathParam("id") Long id) {
		Users entity = Users.findById(id);
		if (entity == null) {
			return new Users();
		}
		return entity;
	}

	@Inject
	ReactiveMailer reactiveMailer;

	@GET
	@Path("/email")
	public CompletionStage<Response> sendASimpleEmailAsync() {
		return reactiveMailer
				.send(Mail.withText("weiwei0620@qq.com", "A reactive email from quarkus", "This is my body"))
				.subscribeAsCompletionStage().thenApply(x -> Response.accepted().build());
	}

}
