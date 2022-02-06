package com.weirblog.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.weirblog.entity.Photo;
import com.weirblog.entity.PhotoImg;
import com.weirblog.util.BaseUtil;
import com.weirblog.util.UserAgentUtil;
import com.weirblog.vo.DataGrid;
import com.weirblog.vo.FileBody;
import com.weirblog.vo.JsonVO;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import net.coobird.thumbnailator.Thumbnailator;

import javax.servlet.http.HttpServletRequest;

@Path("photo")
@ApplicationScoped
public class PhotoController {
	
	@ConfigProperty(name = "photo.img.view.save-path")
	String photoImgViewPath;
	
//	@Context
//    HttpServerRequest request;
	
	private @Context HttpServletRequest request;
	
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
		List<PhotoImg> list = PhotoImg.find("photoId=?1", id).list();
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
		List<PhotoImg> find = PhotoImg.find("photoId=?1", id).list();
		if (UserAgentUtil.checkMobile(request.getHeader("User-Agent"))) {
			return photoViewPhone.data("photoImgs", find);
		}else {
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
		Photo photo = Photo.findById(id);
		return photoAdd.data("photo", photo);
	}

	@GET
	@Path("list")
	public DataGrid<Photo> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		PanacheQuery<Photo> pageQuery = Photo.findAll(Sort.by("createDate", Direction.Descending)).page(page-1, rows);
		DataGrid<Photo> dataGrid = new DataGrid<Photo>(pageQuery.count(), pageQuery.list());
		return dataGrid;
	}
	
	@POST
	@Path("listImg/{photoId}")
	public DataGrid<PhotoImg> listImg(@PathParam("photoId") Integer photoId,PhotoImg photoImg) {
		PanacheQuery<PhotoImg> pageQuery = PhotoImg.find("photo_id = ?1", photoId).page(photoImg.page, photoImg.rows);
		DataGrid<PhotoImg> dataGrid = new DataGrid<PhotoImg>(pageQuery.count(), pageQuery.list());
		return dataGrid;
	}
	@POST
	@Path("addImg/{id}")
	public JsonVO<PhotoImg> addImg(@PathParam("id") Integer id,PhotoImg photoImg) {
		JsonVO<PhotoImg> j = new JsonVO<>();
		
		PhotoImg entity = PhotoImg.findById(id);
		entity.imgDesc = photoImg.imgDesc;
		j.setMsg("修改成功");
		j.setSuccess(true);
		return j;
	}
	
	@POST
	@Path("add")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public JsonVO<Object> add(
			@FormParam("id") String id,
			@FormParam("name") String name) {
		if (StringUtils.isBlank(id)) {
			Photo entity = new Photo();
			entity.name = name;
			entity.persistAndFlush();
		}else {
			Photo entity = Photo.findById(Integer.valueOf(id));
			entity.name = name;
			entity.updateDate = new Date();
		}
		JsonVO<Object> json = new JsonVO<>();
		json.setMsg("添加/修改成功");
		json.setSuccess(true);
		return json;
	}
	
	@DELETE
	@Path("delete")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public JsonVO<Object> delete(@FormParam("photoIds") String photoIds) {
		String[] ids = photoIds.split(",");
		for (String id : ids) {
			Photo.deleteById(Integer.valueOf(id));
		}
		JsonVO<Object> json = new JsonVO<>();
		json.setMsg("相册删除成功");
		json.setSuccess(true);
		return json;
	}
	
	@POST
    @Path("/uploadImg/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Transactional
	public JsonVO<Object> uploadImg(@PathParam("id") Integer id, 
			@MultipartForm FileBody fileBody
			) {
		JsonVO<Object> json = new JsonVO<>();
		Photo photo = Photo.findById(id);
		if (photo == null) {
			return json;
		}
		String savePath = photoImgViewPath + id +"/";
		File uploadDir = new File(savePath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		String saveThumbnailPath = photoImgViewPath + "thumbnail/" + id +"/";
		File uploadThumbnailDir = new File(saveThumbnailPath);
		if (!uploadThumbnailDir.exists()) {
			uploadThumbnailDir.mkdirs();
		}
		String fileName = fileBody.filePath;
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		try {
			InputStream i = fileBody.inputStream.getBody(InputStream.class, null);
			String newFileName = BaseUtil.parseDateToStringNomm(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
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
            photoImg.imgPath = id+"/"+newFileName;
            photoImg.photoId = id;
            photoImg.thumbnail = id+"/"+newFileName;
            photoImg.persist();
			json.setSuccess(true);
		} catch (Exception e) {
			json.setMsg("文件上传失败");
			e.printStackTrace();
		}
		return json;
	}
	@GET
	@Path("topImg/{id}")
	@Transactional
	public JsonVO<Object> topImg(@PathParam("id") Integer id) {
		JsonVO<Object> j = new JsonVO<>();
		PhotoImg photoImg = PhotoImg.findById(id);
		if (photoImg == null) {
			return j;
		}
		Photo p = Photo.findById(photoImg.photoId);
		String thumbnail = photoImg.thumbnail;
		thumbnail = thumbnail.substring(thumbnail.lastIndexOf("/") - 1);
		p.thumbnail = thumbnail;
		p.updateDate = new Date();
		j.setMsg("设置成功");
		j.setSuccess(true);
		return j;
	}
	@GET
	@Path("deletePhoto")
	@Transactional
	public JsonVO<Object> deletePhoto(@PathParam("id") Integer id) {
		JsonVO<Object> j = new JsonVO<>();
		PhotoImg photoImg = PhotoImg.findById(id);
		if (photoImg == null) {
			return j;
		}
		photoImg.delete();
		File file = new File(photoImgViewPath + "/thumbnail/"+photoImg.imgPath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		j.setMsg("图片删除成功");
		j.setSuccess(true);
		return j;
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
	
	
	@POST
    @Path("/upload-demo/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces("application/json;charset=UTF-8")
	@Transactional
	public JsonVO<Object> uploadImgDemo(@PathParam("id") Integer id, 
			@MultipartForm FileBody fileBody
			) {
		JsonVO<Object> json = new JsonVO<>();
//		Photo photo = Photo.findById(id);
//		if (photo == null) {
//			return json;
//		}
		String savePath = photoImgViewPath + id +"/";
		File uploadDir = new File(savePath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		String saveThumbnailPath = photoImgViewPath + "thumbnail/" + id +"/";
		File uploadThumbnailDir = new File(saveThumbnailPath);
		if (!uploadThumbnailDir.exists()) {
			uploadThumbnailDir.mkdirs();
		}
		String fileName = fileBody.filePath;
		System.out.println("---------------------" + fileName);
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		try {
			InputStream i = fileBody.inputStream.getBody(InputStream.class, null);
			
			String[] contentDisposition = fileBody.inputStream.getHeaders().getFirst("Content-Disposition").split(";");
			for (String filename : contentDisposition) {
			    if ((filename.trim().startsWith("filename"))) {
			        String[] name = filename.split("=");
			        fileName = name[1].trim().replaceAll("\"", "");
			    }
			}
			
//			System.out.println("---------------------" + fileName);
			System.out.println("---------------------" + URLDecoder.decode(fileName, "utf-8"));
			
//			String newFileName = BaseUtil.parseDateToStringNomm(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
//			String filePath = savePath + newFileName;
//			OutputStream o = new FileOutputStream(new File(filePath));
//            i.transferTo(o);
//            i.close();
//            o.close();
//            String thumbnailPath = saveThumbnailPath + newFileName;
//            Thumbnailator.createThumbnail(new File(filePath), new File(thumbnailPath), 500, 300);
            
//            PhotoImg photoImg = new PhotoImg();
//            photoImg.imgDesc = photo.name;
//            photoImg.imgName = newFileName;
//            photoImg.imgPath = id+"/"+newFileName;
//            photoImg.photoId = id;
//            photoImg.thumbnail = id+"/"+newFileName;
//            photoImg.persist();
			json.setSuccess(true);
		} catch (Exception e) {
			json.setMsg("文件上传失败");
			e.printStackTrace();
		}
		return json;
	}
}
