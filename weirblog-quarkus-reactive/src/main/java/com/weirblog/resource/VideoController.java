package com.weirblog.resource;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
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

import com.weirblog.entity.Video;
import com.weirblog.vo.DataGrid;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;

@Path("video")
@ApplicationScoped
public class VideoController {

	@Location("admin/videoAdd.html")
	Template videoAdd;

	@GET
	@Path("editUI/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance videoEdit(@PathParam("id") Integer id) {
//		TemplateInstance templateInstance = videoAdd.data("video", Video.findById(id));
//		return Uni.createFrom().completionStage(() -> templateInstance.renderAsync());
		Uni<Video> video = Video.findById(id);
		return videoAdd.data("video", video);
	}

	@GET
	@Path("addUI")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance videoAdd() {
		return videoAdd.data("video", null);
	}

	@GET
	@Path("list")
	public Uni<Object> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		return Panache.withTransaction(() -> {
			PanacheQuery<Video> findAll = Video.findAll(Sort.by("createDate",Direction.Descending));
			Uni<Long> count = findAll.count();
			Uni<List<Video>> list = findAll.page(Page.of(page-1, rows)).list();
			return Uni.combine().all().unis(count, list).combinedWith((c, l) -> new DataGrid<Video>(c, l));
		});
	}

	@POST
	@Path("add")
	@Transactional
	public Uni<Response> add(Video video) {
		if (video.id == null) {
			return Panache.withTransaction(video::persist).replaceWith(Response
					.ok(new JsonObject().put("msg", "新增成功").put("success", true)).status(Status.CREATED)::build);
		} else {
			return Panache.withTransaction(() -> Video.<Video>findById(video.id).onItem().ifNotNull().invoke(entity -> {
				entity.updateDate = new Date();
				entity.vdesc = video.vdesc;
				entity.vname = video.vname;
				entity.vurl = video.vurl;
			})).onItem().ifNotNull()
					.transform(entity -> Response.ok(new JsonObject().put("msg", "修改成功").put("success", true)).build())
					.onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
		}
	}
	@DELETE
	@Path("delete")
	public Uni<Object> delete(@FormParam("videoIds") String videoIds) {
		List<Integer> collect = Arrays.asList(videoIds.split(",")).stream().map(s->Integer.valueOf(s)).collect(Collectors.toList());
		return Panache.withTransaction(()->Video.delete("id in (?1)", collect)).map(deleted -> deleted != null
						? Response.ok(new JsonObject().put("msg", "操作成功").put("success", true)).status(Status.OK)
								.build()
						: Response.ok().status(Status.NOT_FOUND).build());
		
	}
}
