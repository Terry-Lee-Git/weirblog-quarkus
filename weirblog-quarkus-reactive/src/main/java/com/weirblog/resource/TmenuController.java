package com.weirblog.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import com.weirblog.entity.Tmenu;
import com.weirblog.service.TmenuService;
import com.weirblog.vo.MenuVo;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;

@Path("menu")
@ApplicationScoped
public class TmenuController {

	@Inject
	TmenuService tmenuService;

	@GET
	@Path("tree")
	public Uni<List<MenuVo>> tree(@QueryParam("id") Integer id) {
		return Panache.withTransaction(() -> {
			Uni<List<Tmenu>> menuList = null;
			if (id == null) {
				menuList = Tmenu.list("PID is null");
			} else {
				menuList = Tmenu.list("PID = ?1", id);
			}
//			Uni<List<Tmenu>> menuList = Tmenu.list("PID is null");
			Uni<List<MenuVo>> mlist = menuList.chain(list -> {
				List<MenuVo> menus = new ArrayList<MenuVo>();
				for (Tmenu tmenu : list) {
					MenuVo m = new MenuVo();
					m.setId(tmenu.id);
					m.setText(tmenu.text);
					m.setName(tmenu.text);
					m.setIconcls(tmenu.iconcls);
					m.setUrl(tmenu.url);
					m.setPid(tmenu.PID != null ? tmenu.PID : null);
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("url", tmenu.url);
					m.setAttributes(attributes);
					menus.add(m);
				}
				return Uni.createFrom().item(menus);
			});
			// pid有没有子节点
			Uni<List<Tmenu>> listAll = Tmenu.listAll();
			Uni<Map<Integer, Long>> pidMap = listAll.chain(list -> {
				Map<Integer, Long> map = list.parallelStream().filter(m -> m.getPID() != null)
						.collect(Collectors.groupingBy(Tmenu::getPID, Collectors.counting()));
				return Uni.createFrom().item(map);
			});

			return Uni.combine().all().unis(mlist, pidMap).combinedWith((list, map) -> {
				for (MenuVo tmenu : list) {
					Long count = map.get(tmenu.getId());
					if (count != null && count > 0) {
//						System.out.println("--------tmenu---" + tmenu.getId()+"----count-------------" + count);
						tmenu.setState("closed");
					} else {
						tmenu.setState("open");
					}
				}
				return list;
			});
		});
	}

	@GET
	@Path("allTree")
	public Uni<List<MenuVo>> allTree() {
		return Panache.withTransaction(() -> {

			Uni<List<Tmenu>> tmenus = Tmenu.findAll().list();
			Uni<List<MenuVo>> mvos = tmenus.chain(list -> {
				List<MenuVo> menus = new ArrayList<MenuVo>();
				for (Tmenu tmenu : list) {
					MenuVo m = new MenuVo();
					m.setId(tmenu.id);
					m.setIconcls(tmenu.iconcls);
					m.setName(tmenu.text);
					m.setUrl(tmenu.url);
					m.setPid(tmenu.PID);
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("url", tmenu.url);
					m.setAttributes(attributes);
					menus.add(m);
				}
				return Uni.createFrom().item(menus);
			});

			Uni<Map<Integer, Tmenu>> pidMap = tmenus.chain(list -> {
				Map<Integer, Tmenu> map = list.parallelStream().filter(m -> m.getPID() != null)
						.collect(Collectors.toMap(Tmenu::getPID, a -> a, (k1, k2) -> k1));
				return Uni.createFrom().item(map);
			});

			return mvos.chain(list -> pidMap.chain(map -> {
				for (MenuVo tmenu : list) {
					if (tmenu.getPid() != null) {
						Tmenu t = map.get(tmenu.getPid());
						if (t != null) {
							tmenu.setPname(t.text);
						}
					}
				}
				return Uni.createFrom().item(list);
			}));
		});
	}

	@POST
	@Path("add")
	public Uni<Response> add(Tmenu tmenu) {
		if (tmenu.id == null) {
			return Panache.withTransaction(tmenu::persist).replaceWith(Response
					.ok(new JsonObject().put("msg", "新增成功").put("success", true)).status(Status.CREATED)::build);
		} else {
			return Panache.withTransaction(() -> Tmenu.<Tmenu>findById(tmenu.id)
					.onItem()
					.ifNotNull().invoke(entity -> {
				entity.text = tmenu.text;
				entity.url = tmenu.url;
				entity.PID = tmenu.PID;
			}))
					.onItem().ifNotNull()
					.transform(entity -> Response.ok(new JsonObject().put("msg", "修改成功").put("success", true)).build())
					.onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
		}
	}

	@Location("admin/menuAdd.html")
	Template menuAdd;

	@GET
	@Path("addUI")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance menuAdd() {
		return menuAdd.data("tmenu", new Tmenu());
	}

	@Location("admin/menuAdd.html")
	Template menuEdit;

	@GET
	@Path("editUI/{id}")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance menuEdit(@PathParam("id") Integer id) {
		return menuEdit.data("tmenu", Tmenu.findById(id));
	}

	@DELETE
	@Path("delete/{id}")
	public Uni<Response> delete(@PathParam("id") Integer id) {
		return Panache.withTransaction(() -> Tmenu.deleteById(id))
				.map(deleted -> deleted
						? Response.ok(new JsonObject().put("msg", "操作成功").put("success", true)).status(Status.OK)
								.build()
						: Response.ok().status(Status.NOT_FOUND).build());
	}
}
