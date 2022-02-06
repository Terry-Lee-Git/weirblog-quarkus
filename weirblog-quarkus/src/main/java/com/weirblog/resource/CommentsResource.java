package com.weirblog.resource;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.weirblog.entity.Comments;
import com.weirblog.util.IpUtil;
import com.weirblog.vo.DataGrid;
import com.weirblog.vo.JsonVO;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.impl.CookieImpl;

/**
 * 评论管理
 * @author Administrator
 *
 */
@Path("comments")
@ApplicationScoped
public class CommentsResource {

	@Inject
    ReactiveMailer reactiveMailer;
	@Context
    HttpServerRequest request;
	@Context
	HttpServerResponse response;
	
	@POST
	@Path("add")
	@Transactional
	public JsonVO<Object> add(Comments comments) {
		JsonVO<Object> json = new JsonVO<>();
		String ip = IpUtil.getIpAddr(request);
		@Nullable
		Cookie cookie2 = request.getCookie("userIP");
		Map<String, Cookie> cookieMap = request.cookieMap();
		if (cookie2 != null) {			
			Cookie cookie = cookieMap.get(cookie2.getName());
			if (cookie != null && 
					cookie.getValue().equals(cookie2.getValue())) {
				if (cookie.getValue().equals(ip)) {
					json = new JsonVO<>(1,false,"请求多余频繁，请两分钟后在操作");
					return json;
				}
			}
		}
		try {
			comments.persist();
			
			Cookie cookie = new CookieImpl("userIP", ip);
	        cookie.setMaxAge(60*2);//2分钟
	        cookie.setPath("/");
	        response.addCookie(cookie);
	        json.setMsg("评论添加成功");
			json.setSuccess(true);
			
			reactiveMailer.send(
	                Mail.withText("634623907@qq.com", "有人评论", "http://www.loveweir.com/posts/view/"+comments.postsId))
	                .subscribeAsCompletionStage()
	                .thenApply(x -> Response.accepted().build());
		} catch (Exception e) {
			json.setMsg("评论添加失败");
			e.printStackTrace();
		}
		return json;
	}
	@GET
	@Path("list")
	public DataGrid<Comments> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		PanacheQuery<Comments> pageQuery = Comments.findAll(Sort.by("createDate", Direction.Descending)).page(page-1, rows);
		DataGrid<Comments> dataGrid = new DataGrid<Comments>(pageQuery.count(), pageQuery.list());
		return dataGrid;
	}
}
