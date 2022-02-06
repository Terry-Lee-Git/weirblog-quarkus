package com.weirblog.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.reactive.mutiny.Mutiny;

import com.weirblog.entity.Comments;
import com.weirblog.util.IpUtil;
import com.weirblog.vo.JsonVO;
import com.weirblog.vo.page.QueryResultVO;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.impl.CookieImpl;
import io.vertx.mutiny.core.Context;
import io.vertx.mutiny.core.Vertx;

/**
 * 评论管理
 * 
 * @author Administrator
 *
 */
@Path("comments")
@ApplicationScoped
public class CommentsResource {

	@Inject
	ReactiveMailer reactiveMailer;
//	@Context
//	HttpServerRequest request;
//	@Context
//	HttpServerResponse response;
	
//	@POST
//	@Path("add")
//	public JsonVO<Object> add(Comments comments) {
//		JsonVO<Object> json = new JsonVO<>();
//		String ip = IpUtil.getIpAddr(request);
//		@Nullable
//		Cookie cookie2 = request.getCookie("userIP");
//		Map<String, Cookie> cookieMap = request.cookieMap();
//		if (cookie2 != null) {
//			Cookie cookie = cookieMap.get(cookie2.getName());
//			if (cookie != null && cookie.getValue().equals(cookie2.getValue())) {
//				if (cookie.getValue().equals(ip)) {
//					json = new JsonVO<>(1, false, "请求多余频繁，请两分钟后在操作");
//					return json;
//				}
//			}
//		}
//		try {
//			comments.persist();
//			Cookie cookie = new CookieImpl("userIP", ip);
//			cookie.setMaxAge(60 * 2);// 2分钟
//			cookie.setPath("/");
//			response.addCookie(cookie);
//			json.setMsg("评论添加成功");
//			json.setSuccess(true);
//			reactiveMailer
//					.send(Mail.withText("634623907@qq.com", "有人评论",
//							"http://www.loveweir.com/posts/view/" + comments.postsId))
//					.subscribeAsCompletionStage().thenApply(x -> Response.accepted().build());
//		} catch (Exception e) {
//			json.setMsg("评论添加失败");
//			e.printStackTrace();
//		}
//		return json;
//	}

	@POST
	@Path("list")
	public Uni<QueryResultVO<Comments>> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		System.out.println("----------------" + page + "---" + rows);
		PanacheQuery<Comments> panacheQuery = Comments.findAll().page(page - 1, rows);

		Uni<List<Comments>> comments = panacheQuery.list();
		Uni<Long> count = Comments.count();

		Uni<QueryResultVO<Comments>> pageList = Uni.combine().all().unis(comments, count).combinedWith((list, num) -> {
			QueryResultVO<Comments> qr = new QueryResultVO<>();
			qr.resultList = list;
			qr.totalRecord = num;
			return qr;
		});

		return pageList;
	}

	@POST
	@Path("list2")
	public Uni<QueryResultVO<Comments>> list2(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		Uni<List<Comments>> panacheQuery = Comments.findAll().page(page - 1, rows).list();
		Uni<Long> count = Comments.count();
		return Uni.combine().all().unis(panacheQuery, count).combinedWith(this::computeFightOutcome);
	}

	private QueryResultVO<Comments> computeFightOutcome(List<Comments> list, Long count) {
		QueryResultVO<Comments> pageList = new QueryResultVO<>();
		pageList.resultList = list;
		pageList.totalRecord = count;
		return pageList;
	}

	@GET
	@Path("{id}")
	public Uni<Comments> get(@PathParam("id") Integer id) {
		return Comments.findById(id);
	}

	public static void main(String[] args) {
		List<Comments> list = new ArrayList<>();
		for (int i = 0; i < 22; i++) {
			Comments comments = new Comments();
			comments.id = i + 1;
			comments.name = "www" + i;
			list.add(comments);
		}
		List<Long> ids = list.stream().map(Comments::getId).map(s -> Long.valueOf(s.toString()))
				.collect(Collectors.toList());
		System.out.println(ids);
		List<Integer> asList = Arrays.asList(2, 3);
		list = list.stream().filter(p -> !asList.contains(p.getId())).collect(Collectors.toList());
		System.out.println(list.stream().map(Comments::getId).collect(Collectors.toList()));
//		DataGrid<Comments> dataGrid = new DataGrid<>(10l, list);
//		Multi.createFrom().item(dataGrid)
//		.toUni()
//		.subscribe()
//		.with(System.out::println);

	}
}
