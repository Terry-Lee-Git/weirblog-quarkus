package com.weirblog.resource;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.weirblog.entity.Video;
import com.weirblog.vo.DataGrid;
import com.weirblog.vo.JsonVO;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("video")
@ApplicationScoped
public class VideoController {
	
	@Location("admin/videoAdd.html")
	Template videoAdd;
	
	@GET
	@Path("editUI/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance videoEdit(@PathParam("id") Integer id) {
		Video video = Video.findById(id);
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
	public DataGrid<Video> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		PanacheQuery<PanacheEntityBase> panacheQuery = Video.findAll(Sort.by("createDate", Direction.Descending)).page(page-1, rows);
		DataGrid<Video> dataGrid = new DataGrid<Video>(panacheQuery.count(), panacheQuery.list());
		return dataGrid;
	}
	@POST
	@Path("add")
	@Transactional
	public JsonVO<Object> add(
			@FormParam("id") String id,
			@FormParam("vname") String vname,
			@FormParam("vurl") String vurl,
			@FormParam("vdesc") String vdesc) {
		JsonVO<Object> json = new JsonVO<>();
		if (StringUtils.isBlank(id)) {
			Video video = new Video();
			video.vname = vname;
			video.vurl = vurl;
			video.vdesc = vdesc;
			video.persistAndFlush();
		}else {
			Video v = Video.findById(Integer.valueOf(id));
			if (v!=null) {
				v.updateDate = new Date();
				v.vdesc = vdesc;
				v.vname = vname;
				v.vurl = vurl;
			}
		}
		json.setMsg("添加/修改成功");
		json.setSuccess(true);
		return json;
	}
	
}
