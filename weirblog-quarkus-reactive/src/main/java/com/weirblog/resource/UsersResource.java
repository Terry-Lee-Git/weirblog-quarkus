package com.weirblog.resource;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;

import com.weirblog.entity.Users;
import com.weirblog.vo.DataGrid;

import java.util.List;
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
	public Uni<Object> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		return Panache.withTransaction(() -> {
			PanacheQuery<Users> findAll = Users.findAll();
			Uni<Long> count = findAll.count();
			Uni<List<Users>> list = findAll.page(Page.of(page-1, rows)).list();
			return Uni.combine().all().unis(count, list).combinedWith((c, l) -> new DataGrid<Users>(c, l));
		});
	}

	@GET
	@Path("{id}")
	public Uni<Users> getSingle(@PathParam("id") Long id) {
		Uni<Users> entity = Users.findById(id);
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
