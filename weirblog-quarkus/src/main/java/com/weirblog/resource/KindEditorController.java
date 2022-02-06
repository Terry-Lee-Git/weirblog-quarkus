package com.weirblog.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.weirblog.util.BaseUtil;
import com.weirblog.vo.FileBody;
import com.weirblog.vo.FileMdBody;

import io.vertx.core.http.HttpServerRequest;

@ApplicationScoped
@Path("kindeditor")
public class KindEditorController {

	@ConfigProperty(name = "photo.img.save-path")
	String photoImgPath;
	@ConfigProperty(name = "photo.img.url-path")
	String photoImgUrl;
	@Context
	HttpServerRequest request;

	@POST
	@Path("file-upload-md/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Map<String, Object> fileUploadMd(@PathParam("id") Integer id,
			@MultipartForm FileMdBody fileBody) {
		File file = new File(photoImgPath + id);
		file.mkdirs();
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4,mkv");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		extMap.put("audio", "mp3");

		String newFileName = null;
		try {
			InputStream i = fileBody.inputStream.getBody(InputStream.class, null);
			String[] contentDisposition = fileBody.inputStream.getHeaders().getFirst("Content-Disposition").split(";");
			String fileName = "";
			for (String filename : contentDisposition) {
				if ((filename.trim().startsWith("filename"))) {
					String[] name = filename.split("=");
					fileName = name[1].trim().replaceAll("\"", "");
				}
			}
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			newFileName = BaseUtil.parseDateToStringNomm(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
			IOUtils.copy(i, new FileOutputStream(new File(photoImgPath + id + "/" + newFileName)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("success", 1);
		msg.put("message", "ok");
		msg.put("url", photoImgUrl + id + "/" + newFileName);
		return msg;
	}
	@POST
	@Path("file-upload/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Map<String, Object> fileUpload(@PathParam("id") Integer id,
			@MultipartForm FileBody fileBody) {
		File file = new File(photoImgPath + id);
		file.mkdirs();
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4,mkv");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		extMap.put("audio", "mp3");
		
		String newFileName = null;
		try {
			InputStream i = fileBody.inputStream.getBody(InputStream.class, null);
			String[] contentDisposition = fileBody.inputStream.getHeaders().getFirst("Content-Disposition").split(";");
			String fileName = "";
			for (String filename : contentDisposition) {
				if ((filename.trim().startsWith("filename"))) {
					String[] name = filename.split("=");
					fileName = name[1].trim().replaceAll("\"", "");
				}
			}
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			newFileName = BaseUtil.parseDateToStringNomm(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
			IOUtils.copy(i, new FileOutputStream(new File(photoImgPath + id + "/" + newFileName)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("error", 0);
		msg.put("url", photoImgUrl + id + "/" + newFileName);
		return msg;
	}

	@SuppressWarnings("rawtypes")
	class NameComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable) a;
			Hashtable hashB = (Hashtable) b;
			if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String) hashA.get("filename")).compareTo((String) hashB.get("filename"));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	class SizeComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable) a;
			Hashtable hashB = (Hashtable) b;
			if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				if (((Long) hashA.get("filesize")) > ((Long) hashB.get("filesize"))) {
					return 1;
				} else if (((Long) hashA.get("filesize")) < ((Long) hashB.get("filesize"))) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	class TypeComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable) a;
			Hashtable hashB = (Hashtable) b;
			if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String) hashA.get("filetype")).compareTo((String) hashB.get("filetype"));
			}
		}
	}

}
