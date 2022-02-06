package com.weirblog.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.weirblog.entity.Photo;
import com.weirblog.entity.Posts;
import com.weirblog.entity.Video;
import com.weirblog.vo.IndexVo;
import com.weirblog.vo.PostView;
import com.weirblog.vo.page.PageView;
import com.weirblog.vo.page.QueryResult;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/")
@ApplicationScoped
@Produces(MediaType.TEXT_HTML)
public class IndexController {

	@Location("index.html")
	Template index;
	@Location("about.html")
	Template about;
	@Location("me.html")
	Template me;
	@Location("photoList.html")
	Template photoList;
	@Location("videoList.html")
	Template videoList;
	@Location("postView.html")
	Template postView;

	@Inject
	EntityManager em;
	/**
	 * 文章标签
	 * @return
	 */
	public Map<String, Long> getTypes() {
		String singleResult = em.createNativeQuery("select GROUP_CONCAT(types) from Posts").getSingleResult()
				.toString();
		List<String> split = stringSplit(singleResult);
		Map<String, Long> map = split.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		return sortByValue(map);
	}
	/**
	 * map 根据value排序
	 * @param <K>
	 * @param <V>
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed())
				.forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}
	/**
	 * 字符串逗号分隔
	 * @param str
	 * @return
	 */
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

//	@CacheResult(cacheName = "post-view")
	@GET
	@Path("view/{id}")
	public TemplateInstance postView(@PathParam("id") Integer id) {
		PostView postViewVo = new PostView();
		Posts posts = Posts.findById(id);
		List<Posts> rightNews = Posts.findAll(Sort.by("createDate", Direction.Descending)).page(0, 10).list();
		List<Posts> rightReads = Posts.findAll(Sort.by("readNum", Direction.Descending)).page(0, 10).list();
		postViewVo.rightNews = rightNews;
		postViewVo.rightReads = rightReads;
		postViewVo.posts = posts;
		return postView.data("postView", postViewVo);
	}

	@GET
	public TemplateInstance wei(@QueryParam("page") Integer page) {
		page = page != null ? page : 1;
		PageView<Posts> pageView = new PageView<Posts>(10, page);
		PanacheQuery<Posts> panacheQuery = Posts.findAll(Sort.by("createDate", Direction.Descending)).page(page - 1,
				10);
		QueryResult<Posts> queryResult = new QueryResult<Posts>();
		queryResult.setTotalRecord(Long.valueOf(panacheQuery.count()).intValue());
		queryResult.setResultList(panacheQuery.list());
		pageView.setQueryResult(queryResult);

		List<Posts> rightReads = Posts.findAll(Sort.by("readNum", Direction.Descending)).page(page - 1, 10).list();

		IndexVo indexVo = new IndexVo();
//		indexVo.setIndex("wei");
		indexVo.setPageView(pageView);
		indexVo.setRightNews(queryResult.getResultList());
		indexVo.setRightReads(rightReads);
		indexVo.setMapType(getTypes());
		return index.data("indexVo", indexVo);
	}

	@GET
	@Path("{u}")
	public TemplateInstance get(@PathParam("u") String u, @QueryParam("page") Integer page,
			@QueryParam("keyWord") String keyWord) {
		page = page != null ? page : 1;
		if (StringUtils.isBlank(u)) {
			return index.data("indexVo", null);
		}
		List<Posts> rightNews = Posts.findAll(Sort.by("createDate", Direction.Descending)).page(0, 10).list();
		List<Posts> rightReads = Posts.findAll(Sort.by("readNum", Direction.Descending)).page(0, 10).list();
		IndexVo indexVo = new IndexVo();
		if (u.equals("about")) {
			indexVo.setIndex("about");
			indexVo.setRightNews(rightNews);
			indexVo.setRightReads(rightReads);
			return about.data("indexVo", indexVo);
		}
		if (u.equals("me")) {
			return me.data(null);
		}
		if (u.equals("photos")) {
			List<Photo> list = Photo.findAll().list();
			return photoList.data("photos", list);
		}
		if (u.equals("videos")) {
			List<Video> list = Video.findAll().list();
			return videoList.data("videos", list);
		}
		PageView<Posts> pageView = new PageView<Posts>(10, page);
		QueryResult<Posts> queryResult = new QueryResult<Posts>();
		if (u.equals("search")) {
			PanacheQuery<Posts> panacheQuery = Posts.find("postTitle like ?1 or description like ?2",
					Sort.by("createDate", Direction.Descending), "%" + keyWord + "%", "%" + keyWord + "%")
					.page(page - 1, 10);
			queryResult.setTotalRecord(Long.valueOf(panacheQuery.count()).intValue());
			queryResult.setResultList(panacheQuery.list());
			pageView.setQueryResult(queryResult);
			indexVo.setPageView(pageView);
			indexVo.setRightNews(rightNews);
			indexVo.setRightReads(rightReads);
			indexVo.setMapType(getTypes());
			indexVo.setIndex("search");
			return index.data("indexVo", indexVo);
		}
		if (u.equals("search-type")) {
			PanacheQuery<Posts> panacheQuery = Posts.find("types like ?1",
					Sort.by("createDate", Direction.Descending), "%" + keyWord + "%")
					.page(page - 1, 10);
			queryResult.setTotalRecord(Long.valueOf(panacheQuery.count()).intValue());
			queryResult.setResultList(panacheQuery.list());
			pageView.setQueryResult(queryResult);
			indexVo.setPageView(pageView);
			indexVo.setRightNews(rightNews);
			indexVo.setRightReads(rightReads);
			indexVo.setMapType(getTypes());
			indexVo.setIndex("search-type");
			return index.data("indexVo", indexVo);
		}

		PanacheQuery<Posts> panacheQuery = Posts
				.find("types like ?1", Sort.by("createDate", Direction.Descending), "%" + u + "%").page(page - 1, 10);
		queryResult.setTotalRecord(Long.valueOf(panacheQuery.count()).intValue());
		queryResult.setResultList(panacheQuery.list());
		pageView.setQueryResult(queryResult);
		indexVo.setPageView(pageView);
		indexVo.setRightNews(rightNews);
		indexVo.setRightReads(rightReads);
		indexVo.setMapType(getTypes());
		if (u.equals("weir")) {
			indexVo.setIndex("weir");
			return index.data("indexVo", indexVo);
		}
		if (u.equals("java")) {
			indexVo.setIndex("java");
			return index.data("indexVo", indexVo);
		}
		return null;
	}

	@ConfigProperty(name = "photo.img.save-path")
	String photoImgPath;

	@GET
	@Path("/img/{name}")
	public Response getAvatar(@PathParam("name") String name) {
		final File file = new File(photoImgPath + name);
		return Response.ok(file).cacheControl(new CacheControl()).build();
	}

	@GET
	@Path("view/{id}/{name}")
	public Response getAvatar(@PathParam("id") Integer id, @PathParam("name") String name) {
		final File file = new File(photoImgPath + id + "/" + name);
		return Response.ok(file).cacheControl(new CacheControl()).build();
	}

	@GET
	@Path("view/{id}/{show}/{name}")
	public Response getAvatar(@PathParam("id") Integer id, @PathParam("show") String show,
			@PathParam("name") String name) {
		final File file = new File(photoImgPath + id + "/" + show + "/" + name);
		return Response.ok(file).cacheControl(new CacheControl()).build();
	}
}
