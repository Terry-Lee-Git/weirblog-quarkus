package com.weirblog.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.weirblog.entity.Photo;
import com.weirblog.entity.PhotoImg;
import com.weirblog.util.BaseUtil;
import com.weirblog.util.UserAgentUtil;
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
import io.smallrye.mutiny.Uni;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import net.coobird.thumbnailator.Thumbnailator;

@Path("photo")
@ApplicationScoped
public class PhotoController {

	@ConfigProperty(name = "photo.img.view.save-path")
	String photoImgViewPath;

//	@Context
//    HttpServerRequest request;

	@Context
	RoutingContext rc;

	@Location("admin/photoAdd.html")
	Template photoAdd;
	@Location("admin/photoUpload.html")
	Template photoUpload;
	@Location("admin/photoView.html")
	Template photoAdminView;

	@Location("photoViewPhone.html")
	Template photoViewPhone;
	@Location("photoView.html")
	Template photoView;

	@GET
	@Path("view/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance photoAdminView(@PathParam("id") Integer id) {
//		List<PhotoImg> list = PhotoImg.find("photoId=?1", id).list();
		Uni<List<PhotoImg>> list = PhotoImg.find("photoId=?1", id).list();
		return photoAdminView.data("photoImgs", list);
	}

	@GET
	@Path("uploadUI/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance photoUpload(@PathParam("id") Integer id) {
		return photoUpload.data("photo", new Photo(id));
	}

	@GET
	@Path("photoMain/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance photoMain(@PathParam("id") Integer id) {
//		List<PhotoImg> find = PhotoImg.find("photoId=?1", id).list();
		Uni<List<PhotoImg>> find = PhotoImg.find("photoId=?1", id).list();
		@Nullable
		String userAgent = rc.request().getHeader("User-Agent"); // request.getHeader("User-Agent")
		if (UserAgentUtil.checkMobile(userAgent)) {
			return photoViewPhone.data("photoImgs", find);
		} else {
			return photoView.data("photoImgs", find);
		}
	}

	@GET
	@Path("addUI")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance photoAdd() {
		return photoAdd.data("photo", null);
	}

	@GET
	@Path("editUI/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance photoEdit(@PathParam("id") Integer id) {
		Uni<Photo> photo = Photo.findById(id);
//		Photo photo = Photo.findById(id);
		return photoAdd.data("photo", photo);
	}

	@GET
	@Path("list")
	public Uni<Object> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		return Panache.withTransaction(() -> {
			PanacheQuery<Photo> findAll = Photo.findAll(Sort.by("createDate", Direction.Descending));
			Uni<Long> count = findAll.count();
			Uni<List<Photo>> list = findAll.page(Page.of(page - 1, rows)).list();
			return Uni.combine().all().unis(count, list).combinedWith((c, l) -> new DataGrid<Photo>(c, l));
		});
	}

	@POST
	@Path("listImg/{photoId}")
	public Uni<Object> listImg(@PathParam("photoId") Integer photoId, PhotoImg photoImg) {
//		PanacheQuery<PhotoImg> pageQuery = PhotoImg.find("photo_id = ?1", photoId).page(photoImg.page, photoImg.rows);
//		DataGrid<PhotoImg> dataGrid = new DataGrid<PhotoImg>(pageQuery.count(), pageQuery.list());
//		return dataGrid;

		return Panache.withTransaction(() -> {
			PanacheQuery<Photo> findAll = Photo.find("photo_id = ?1", photoId);
			Uni<Long> count = findAll.count();
			Uni<List<Photo>> list = findAll.page(Page.of(photoImg.page, photoImg.rows)).list();
			return Uni.combine().all().unis(count, list).combinedWith((c, l) -> new DataGrid<Photo>(c, l));
		});
	}

	@POST
	@Path("addImg/{id}")
	public Uni<Response> addImg(@PathParam("id") Integer id, PhotoImg photoImg) {
		photoImg.id = id;
		return Panache
				.withTransaction(() -> PhotoImg.<PhotoImg>findById(photoImg.id).onItem().ifNotNull().invoke(entity -> {
					entity.imgDesc = photoImg.imgDesc;
				})).onItem().ifNotNull()
				.transform(entity -> Response.ok(new JsonObject().put("msg", "修改成功").put("success", true)).build())
				.onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
	}

	@POST
	@Path("add")
	@Transactional
	public Uni<Response> add(Photo photo) {
		if (photo.id == null) {
			return Panache.withTransaction(photo::persist).replaceWith(Response
					.ok(new JsonObject().put("msg", "新增成功").put("success", true)).status(Status.CREATED)::build);
		} else {
			return Panache.withTransaction(() -> Photo.<Photo>findById(photo.id).onItem().ifNotNull().invoke(entity -> {
				entity.updateDate = new Date();
				entity.name = photo.name;
			})).onItem().ifNotNull()
					.transform(entity -> Response.ok(new JsonObject().put("msg", "修改成功").put("success", true)).build())
					.onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
		}
	}

	@DELETE
	@Path("delete")
	public Uni<Object> delete(@FormParam("photoIds") String photoIds) {
		List<Integer> collect = Arrays.asList(photoIds.split(",")).stream().map(s -> Integer.valueOf(s))
				.collect(Collectors.toList());
		return Panache.withTransaction(() -> Photo.delete("id in (?1)", collect))
				.map(deleted -> deleted != null
						? Response.ok(new JsonObject().put("msg", "操作成功").put("success", true)).status(Status.OK)
								.build()
						: Response.ok().status(Status.NOT_FOUND).build());

	}

	@POST
	@Path("/uploadImg/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Transactional
	public Uni<Response> uploadImg(@PathParam("id") Integer id, @MultipartForm FileBody fileBody) {

		return Panache.withTransaction(() -> Photo.<Photo>findById(id)).onItem().ifNotNull().invoke(photo -> {
			String savePath = photoImgViewPath + id + "/";
			File uploadDir = new File(savePath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
			String saveThumbnailPath = photoImgViewPath + "thumbnail/" + id + "/";
			File uploadThumbnailDir = new File(saveThumbnailPath);
			if (!uploadThumbnailDir.exists()) {
				uploadThumbnailDir.mkdirs();
			}
			String fileName = fileBody.filePath;
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			try {
				InputStream i = fileBody.inputStream.getBody(InputStream.class, null);
				String newFileName = BaseUtil.parseDateToStringNomm(new Date()) + "_" + new Random().nextInt(1000) + "."
						+ fileExt;
				String filePath = savePath + newFileName;
				OutputStream o = new FileOutputStream(new File(filePath));
				i.transferTo(o);
				i.close();
				o.close();
				String thumbnailPath = saveThumbnailPath + newFileName;
				Thumbnailator.createThumbnail(new File(filePath), new File(thumbnailPath), 500, 300);
				PhotoImg photoImg = new PhotoImg();
				photoImg.imgDesc = photo.name;
				photoImg.imgName = newFileName;
				photoImg.imgPath = id + "/" + newFileName;
				photoImg.photoId = id;
				photoImg.thumbnail = id + "/" + newFileName;
				photoImg.persistAndFlush().subscribeAsCompletionStage().toCompletableFuture();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).onItem().ifNotNull()
				.transform(entity -> Response.ok(new JsonObject().put("msg", "上传成功").put("success", true)).build())
				.onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
	}

	@GET
	@Path("topImg/{id}")
	@Transactional
	public Uni<Response> topImg(@PathParam("id") Integer id) {

		return Panache.withTransaction(() -> PhotoImg.<PhotoImg>findById(id).onItem().ifNotNull().invoke(photoImg -> {

			String thumbnail = photoImg.thumbnail;
			thumbnail = thumbnail.substring(thumbnail.lastIndexOf("/") - 1);

			Photo.update("thumbnail=?1,updateDate=?2 where id=?3", thumbnail, new Date(), photoImg.photoId)
					.subscribeAsCompletionStage().toCompletableFuture();

		})).onItem().ifNotNull()
				.transform(entity -> Response.ok(new JsonObject().put("msg", "修改成功").put("success", true)).build())
				.onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);

		// id 数据库存在 程序不报错 正确执行，如果id不存在，没法捕获异常
//		return Panache.withTransaction(()->{
//			Uni<PhotoImg> photoImg = PhotoImg.findById(id);
//			Uni<Photo> photo = photoImg.onItem()
//					.transformToUni(entity-> Photo.findById(entity.photoId));
//			
//			return Uni.combine().all().unis(photoImg,photo)
//					.combinedWith((pImg,p)->{
//						String thumbnail = pImg.thumbnail;
//						thumbnail = thumbnail.substring(thumbnail.lastIndexOf("/") - 1);
//						p.thumbnail = thumbnail;
//						p.updateDate = new Date();
//						return Response.ok(new JsonObject().put("msg", "修改成功").put("success", true)).build();
//					}).replaceIfNullWith(Response.ok(new JsonObject().put("msg", "修改失败").put("success", false)).status(Status.NOT_FOUND)::build);
//		});
	}

	@GET
	@Path("deletePhoto")
	@Transactional
	public Uni<Object> deletePhoto(@PathParam("id") Integer id) {
		return Panache.withTransaction(() -> PhotoImg.deleteById(id))
				.map(deleted -> deleted != null
						? Response.ok(new JsonObject().put("msg", "操作成功").put("success", true)).status(Status.OK)
								.build()
						: Response.ok().status(Status.NOT_FOUND).build());
	}

	@GET
	@Path("imgView/{id}/{name}")
	public Response imgView(@PathParam("id") Integer id, @PathParam("name") String name) {
		File file = new File(photoImgViewPath + id + "/" + name);
		return Response.ok(file).cacheControl(new CacheControl()).build();
	}

	@GET
	@Path("imgThumbnailView/{id}/{name}")
	public Response imgThumbnailView(@PathParam("id") Integer id, @PathParam("name") String name) {
		File file = new File(photoImgViewPath + "thumbnail/" + id + "/" + name);
		return Response.ok(file).cacheControl(new CacheControl()).build();
	}

}
