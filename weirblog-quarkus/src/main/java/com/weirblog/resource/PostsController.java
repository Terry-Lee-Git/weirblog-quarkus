package com.weirblog.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.weirblog.entity.Posts;
import com.weirblog.util.BaseUtil;
import com.weirblog.util.ImagesUtils;
import com.weirblog.vo.DataGrid;
import com.weirblog.vo.FileBody;
import com.weirblog.vo.JsonVO;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
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
	EntityManager em;
	
	@GET
	@Path("/get-types")
	public Response getTypes() {
		String singleResult = em.createNativeQuery("select GROUP_CONCAT(types) from Posts").getSingleResult()
				.toString();
		List<String> split = stringSplit(singleResult);
		Map<String, Long> map = split.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		return Response.status(Status.OK).entity(new JsonObject().put("map", map))
				.build();
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
			}else {				
				lis.add(url);
			}
		}
		return lis;
	}
	
	@Transactional
	@POST
	@Path("file-upload/{id}")
	public Response fileUpload(@PathParam("id") Integer id, @MultipartForm FileBody fileBody) {
		JsonVO<Object> json = new JsonVO<>();
		File file = new File(photoImgPath + id + "/show");
		file.mkdirs();
		String newFileName = null;
		String newViewPath = null;
		try {
			InputStream i = fileBody.inputStream.getBody(InputStream.class, null);
			String fileName = fileBody.filePath;
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			newFileName = BaseUtil.parseDateToStringNomm(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
//			IOUtils.copy(i, new FileOutputStream(new File(photoImgPath + id + "/show/" + newFileName)));
			String newFile = photoImgPath + id + "/show/" + newFileName;
//			Thumbnailator.createThumbnail(i, new FileOutputStream(new File(newFile)), 500, 300);
			
			String srcImageFile = photoImgPath + id + "/show/" + fileName;
			Files.copy(i, Paths.get(srcImageFile));
			ImagesUtils.scale2(srcImageFile, newFile, 500, 300, false);
			Posts posts = Posts.findById(id);
			newViewPath = photoImgUrl + id + "/show/" + newFileName;
			if(posts != null) {
				posts.postPic = newViewPath;
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("文件上传失败");
			json.setSuccess(false);
			return null;
		}
		return Response.status(Status.OK).entity(new JsonObject().put("url", newViewPath))
				.build();
	}

	@GET
	@Path("post-add")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance postAdd() {
		EntityManager entityManager = new Posts().getEntityManager();
		Object result = entityManager.createNativeQuery("select max(id) from Posts").getSingleResult();
		Integer id = null;
		if (result == null) {
			id = 1;
		} else {
			id = Integer.valueOf(result.toString()) + 1;
		}
		Posts posts = new Posts();
		posts.id = id;
		return postAdd.data("posts", posts);
	}
	@GET
	@Path("post-add-md")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance postAddMd() {
		EntityManager entityManager = new Posts().getEntityManager();
		Object result = entityManager.createNativeQuery("select max(id) from Posts").getSingleResult();
		Integer id = null;
		if (result == null) {
			id = 1;
		} else {
			id = Integer.valueOf(result.toString()) + 1;
		}
		Posts posts = new Posts();
		posts.id = id;
		return postAddMd.data("posts", posts);
	}

	@GET
	@Path("post-edit/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance postEdit(@PathParam("id") Integer id) {
		Posts posts = Posts.findById(id);
		return postAddMd.data("posts", posts);
	}
	@GET
	@Path("post-show/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance postShow(@PathParam("id") Integer id) {
		Posts posts = Posts.findById(id);
		return postShow.data("posts", posts);
	}

	@GET
	@Path("pnum/{id}")
	@Transactional
	public void pnum(@PathParam("id") Integer id) {
		Posts p = Posts.findById(id);
		p.readNum = p.readNum + 1;
	}

	@POST
	@Path("add")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String add(@FormParam("ptitle") String postTitle, @FormParam("types") String types,
			@FormParam("description") String description, @FormParam("content") String content,
			@FormParam("id") Integer id) {
		if (StringUtils.isNotBlank(description) && description.length() > 100) {
			description = description.substring(0, 99);
		}
		if (id == null) {
			Object result = Posts.getEntityManager().createNativeQuery("select max(id) from Posts").getSingleResult();
//			EntityManager entityManager = new Posts().getEntityManager();
//			Object result = entityManager.createNativeQuery("select max(id) from Posts").getSingleResult();
			if (result == null) {
				id = 1;
			} else {
				id = Integer.valueOf(result.toString()) + 1;
			}
			Posts entity = new Posts();
			entity.id = id;
			entity.postTitle = postTitle;
			entity.types = types;
			entity.description = description;
			entity.content = content;
			entity.persistAndFlush();
		} else {
			Posts entity = Posts.findById(id);
			boolean flag = true;
			if (entity == null) {
				entity = new Posts();
				entity.id = id;
			} else {
				flag = false;
			}
			entity.postTitle = postTitle;
			entity.types = types;
			entity.description = description;
			entity.content = content;
			if (flag) {
				entity.persistAndFlush();
			}
		}
		return "ok";
	}

	@GET
	@Path("list")
	public DataGrid<Posts> list(@QueryParam("page") Integer page, @QueryParam("rows") Integer rows) {
		PanacheQuery<Posts> panacheQuery = Posts.findAll(Sort.by("createDate", Direction.Descending)).page(page - 1,
				rows);
		DataGrid<Posts> dataGrid = new DataGrid<Posts>(panacheQuery.count(), panacheQuery.list());
		return dataGrid;
	}

	@GET
	@Path("edit-content")
	@Transactional
	public String editContent() {
		String prePath = "/Users/weir";
		List<Posts> list = Posts.findAll().list();
		for (Posts posts : list) {
			Document doc = Jsoup.parse(posts.content);
			Elements imgSrcs = doc.select("img[src]");
			if (!imgSrcs.isEmpty()) {
				for (Element element : imgSrcs) {
					// /attached/image/20200910/20200910180201_945.png to
					// /attached/image/{id}/20200910180201_945.png
					String src = element.attr("src");
					String pre = src.substring(0, src.lastIndexOf("/"));
					String preRel = src.replace(pre, posts.id.toString());
					element.attr("src", preRel);
					File file = new File(prePath + pre);
					copyFile(file.listFiles(), posts.id);
				}
				posts.content = doc.body().html();
			}
//			System.out.println("----------" + posts.postTitle);
		}
		return "ok";
	}

	@GET
	@Path("edit-content-show")
	@Transactional
	public String editContentForShow() {
		
		File files = new File(photoImgPath);
		for (File file : files.listFiles()) {
			if (file == null || file.listFiles() == null) {
				continue;
			}
			String id = file.getName();
			for(File f : file.listFiles()) {
//				System.out.println(file.getName() +"-------------" + f.getName());
				File filep = new File(photoImgPath + id + "/show");
				filep.mkdirs();
				File srcFile = new File(photoImgPath + id + "/show/" + f.getName());
				copyFileUsingFiles(f, srcFile);
				
				Posts posts = Posts.findById(Integer.valueOf(id));
				posts.postPic = "/view/" + id + "/show/" + f.getName();
				break;
			}
		}
		
		return "ok";
	}
	@GET
	@Path("show-pic")
	@Transactional
	public String showPic() {
		
		File files = new File(photoImgPath);
		for (File file : files.listFiles()) {
			if (file == null || file.listFiles() == null) {
				continue;
			}
			for(File f : file.listFiles()) {
				if (f.isDirectory()) {
//					System.out.println("------11---------" + f.getPath());
					for(File show : f.listFiles()) {
//						System.out.println("-------22--------" + show.getName());
						try {
							Thumbnailator.createThumbnail(show, show, 500, 300);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		return "ok";
	}

	public static void copyFileUsingFiles(File src, File dest) {
		try {
			Files.copy(src.toPath(), dest.toPath());
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}

	private void copyFile(File[] listFiles, Integer id) {
		if (listFiles == null || listFiles.length == 0) {
			return;
		}
		if (listFiles != null && listFiles.length > 0) {
			for (File file3 : listFiles) {
				File newFile = new File(photoImgPath + id + "/");
				newFile.mkdirs();
				newFile = new File(photoImgPath + id + "/" + file3.getName());
				try {
					Files.copy(file3.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

//	public static void main(String[] args) throws Exception {
//		String mp3Path = "/Users/weir/Downloads/33/";
//		String url = "http://resources.bitpress.com.cn/resources/songcc/978-7-5682-8500-1/WEB/";
////		Jsoup.parse(new URL("http://resources.bitpress.com.cn/resources/songcc/978-7-5682-8500-1/WEB/default.html"), 0)
//		Document document = Jsoup
//				.connect("http://resources.bitpress.com.cn/resources/songcc/978-7-5682-8500-1/WEB/default.html").get();
//		Elements select = document.select("a[href]");
//		for (Element element : select) {
//			String attr = element.attr("href");
//			attr = URLDecoder.decode(attr, "utf-8");
//			String nameMp3 = attr.substring(attr.lastIndexOf("/"));
//			System.out.println("-----1----" + attr);
////			downLoadMp3FromUrl(url + attr, mp3Path + nameMp3);
////			attr = URLEncoder.encode(attr, "utf-8");
//			attr = encodeUrl(attr);
////			System.out.println("-----2----" + attr);
//			saveToFile(url + attr, mp3Path + nameMp3);
//		}
//	}

	public static String encodeUrl(String lastUrl) throws Exception {
		String[] split = lastUrl.split("/");
		StringBuilder sb = new StringBuilder();
		for (int i = 0, h = split.length; i < h; i++) {
			String[] split2 = split[i].split(" ");
			if (split2.length > 1) {
				for (int j = 0, h1 = split2.length; j < h1; j++) {
					if ((j + 1) < h1) {
						sb.append(URLEncoder.encode(split2[j], "utf-8")).append("%20");
					} else {
						sb.append(URLEncoder.encode(split2[j], "utf-8"));
					}
				}
			} else {
				if ((i + 1) < h) {
					sb.append(URLEncoder.encode(split[i], "utf-8")).append("/");
				} else {
					sb.append(URLEncoder.encode(split[i], "utf-8"));
				}
			}
		}
		return sb.toString();
	}

	public static void downLoadMp3FromUrl(String downloadUrl, String filename) throws Exception {
		URL download = new URL(downloadUrl);
		URLConnection con = download.openConnection();
		InputStream is = con.getInputStream();
		byte[] bs = new byte[1024];
		int len;
		OutputStream os = new FileOutputStream(filename);
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		os.close();
		is.close();
	}

	public static void saveToFile(String destUrl, String fileName) throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpconn = null;
		URL url = null;
		byte[] buf = new byte[1024];
		int size = 0;

		// 建立链接
		url = new URL(destUrl);
		httpconn = (HttpURLConnection) url.openConnection();
		// 连接指定的资源
		httpconn.connect();
		// 获取网络输入流
		bis = new BufferedInputStream(httpconn.getInputStream());
		// 建立文件
		fos = new FileOutputStream(fileName);

		System.out.println("正在获取链接[" + destUrl + "]的内容\n将其保存为文件[" + fileName + "]");

		// 保存文件
		while ((size = bis.read(buf)) != -1)
			fos.write(buf, 0, size);
		fos.close();
		bis.close();
		httpconn.disconnect();
	}
}
