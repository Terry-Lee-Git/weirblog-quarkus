package com.weirblog.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.weirblog.entity.Posts;
import com.weirblog.util.BaseUtil;
import com.weirblog.vo.DataGrid;
import com.weirblog.vo.FileBody;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import net.coobird.thumbnailator.Thumbnailator;

/**
 * 文章管理
 * 
 * @author Administrator
 *
 */
@Path("posts")
@ApplicationScoped
public class PostsController {
	@ConfigProperty(name = "photo.img.save-path")
	String photoImgPath;
	@ConfigProperty(name = "photo.img.url-path")
	String photoImgUrl;

	@Location("admin/postAdd.html")
	Template postAdd;
	@Location("admin/postAdd_md.html")
	Template postAddMd;
	@Location("admin/postShow.html")
	Template postShow;

	@Inject
	Mutiny.Session session;
	@Inject
	Mutiny.SessionFactory sessionFactory;

	@GET
	@Path("/get-types")
	public Response getTypes() {
		String singleResult = "";
//				session.createNativeQuery("select GROUP_CONCAT(types) from Posts").getSingleResult()
//				.toString();
		List<String> split = stringSplit(singleResult);
		Map<String, Long> map = split.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		return Response.status(Status.OK).entity(new JsonObject().put("map", map)).build();
	}

	public static List<String> stringSplit(String str) {
		List<String> lis = new ArrayList<>();
		String[] split = str.split(",");
		for (int i = 0; i < split.length; i++) {
			String url = split[i];
			String[] split2 = url.split("，");
			if (split2 != null && split2.length > 1) {
				for (int i2 = 0; i2 < split2.length; i2++) {
					lis.add(split2[i2]);
				}
			} else {
				lis.add(url);
			}
		}
		return lis;
	}

	@Transactional
	@POST
	@Path("file-upload/{id}")
	public Uni<Response> fileUpload(@PathParam("id") Integer id, @MultipartForm FileBody fileBody) {
		return Panache.withTransaction(()-> Posts.<Posts>findById(id).onItem().ifNotNull().invoke(entity->{
			File file = new File(photoImgPath + id + "/show");
			file.mkdirs();
			String newFileName = null;
			String newViewPath = null;
			try {
				InputStream i = fileBody.inputStream.getBody(InputStream.class, null);
				String fileName = fileBody.filePath;
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				newFileName = BaseUtil.parseDateToStringNomm(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
				String newFile = photoImgPath + id + "/show/" + newFileName;
				Thumbnailator.createThumbnail(i, new FileOutputStream(new File(newFile)), 500, 300);
				newViewPath = photoImgUrl + id + "/show/" + newFileName;
				entity.postPic = newViewPath;
			} catch (Exception e) {
				e.printStackTrace();
			}
		})).onItem().ifNotNull()
		.transform(entity -> Response.ok(new JsonObject().put("msg", "上传成功").put("success", true)).build())
		.onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
	}

	@GET
	@Path("post-add")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance postAdd() {
//		Uni<Posts> posts = Panache.withTransaction(() -> {
//			Uni<Object> singleResult = session.createNativeQuery("select max(id) from Posts").getSingleResult();
//			Uni<Posts> posts2 = Uni.createFrom().item(new Posts());
//			return Uni.combine().all().unis(singleResult, posts2).combinedWith((sr, p) -> {
//				if (sr == null) {
//					p.id = 1;
//				} else {
//					p.id = Integer.valueOf(sr.toString()) + 1;
//				}
//				return p;
//			});
//		});
		Multi<Object> posts = Panache.withTransaction(()->{
			return session.createNativeQuery("select max(id) from Posts").getSingleResult();
		}).onItem().ifNotNull().transformToMulti(obj-> Multi.createFrom().item(new Posts(Integer.valueOf(obj.toString()))));
		TemplateInstance templateInstance = postAdd.data("posts", posts);
		return templateInstance;
	}

	@GET
	@Path("post-edit/{id}")
	@Produces(MediaType.TEXT_HTML)
	public Uni<String> postEdit(@PathParam("id") Integer id) {
		TemplateInstance templateInstance = postAdd.data("posts", Posts.findById(id));
		return Uni.createFrom().completionStage(() -> templateInstance.renderAsync());
	}

	@GET
	@Path("post-show/{id}")
	@Produces(MediaType.TEXT_HTML)
	public Uni<String> postShow(@PathParam("id") Integer id) {
//		return postShow.data("posts", Posts.findById(id));
		TemplateInstance templateInstance = postShow.data("posts", Posts.findById(id));
		return Uni.createFrom().completionStage(() -> templateInstance.renderAsync());
	}

	@GET
	@Path("pnum/{id}")
	@Transactional
	public Uni<Response> pnum(@PathParam("id") Integer id) {
		return Panache.withTransaction(() -> Posts.<Posts>findById(id).onItem().ifNotNull().invoke(entity -> {
			entity.readNum = entity.readNum + 1;
		})).onItem().ifNotNull()
				.transform(entity -> Response.ok(new JsonObject().put("msg", "修改成功").put("success", true)).build())
				.onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
	}

	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Uni<Response> add(@FormParam("ptitle") String postTitle, @FormParam("types") String types,
			@FormParam("description") String description, @FormParam("content") String content,
			@FormParam("id") Integer id) {
		Posts posts = new Posts();
		posts.postTitle = postTitle;
		posts.types = types;
		posts.description = description;
		posts.content = content;
		posts.id = id;
		if (StringUtils.isNotBlank(description) && description.length() > 100) {
			description = description.substring(0, 99);
		}
		posts.description = description;
		return Panache.withTransaction(posts::persist).replaceWith(
				Response.ok(new JsonObject().put("msg", "新增成功").put("success", true)).status(Status.CREATED)::build);
	}

	@POST
	@Path("edit")
	public Uni<Response> edit(Posts posts) {
		return Panache.withTransaction(() -> Posts.<Posts>findById(posts.id).onItem().ifNotNull().invoke(entity -> {
			entity.postTitle = posts.postTitle;
			entity.types = posts.types;
			entity.description = posts.description;
			entity.content = posts.content;
		})).onItem().ifNotNull()
				.transform(entity -> Response.ok(new JsonObject().put("msg", "修改成功").put("success", true)).build())
				.onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
	}

	@GET
	@Path("list")
	public Uni<Object> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		return Panache.withTransaction(() -> {
			PanacheQuery<Posts> findAll = Posts.findAll(Sort.by("createDate", Direction.Descending));
			Uni<Long> count = findAll.count();
			Uni<List<Posts>> list = findAll.page(Page.of(page - 1, rows)).list();
			return Uni.combine().all().unis(count, list).combinedWith((c, l) -> new DataGrid<Posts>(c, l));
		});
	}

}
