package com.weirblog.resource;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.weirblog.entity.Tmenu;
import com.weirblog.service.TmenuService;
import com.weirblog.vo.JsonVO;
import com.weirblog.vo.MenuVo;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("menu")
@ApplicationScoped
public class TmenuController {

	@Inject
	TmenuService tmenuService;
	
	@GET
	@Path("tree")
	public List<MenuVo> tree(@QueryParam("id") Integer id) {
		if (id == null) {
			id = 0;
		}
		return tmenuService.tree(id);
	}

	@GET
	@Path("allTree")
	public List<MenuVo> allTree() {
		List<Tmenu> tmenus = Tmenu.findAll().list();
		List<MenuVo> menus = new ArrayList<MenuVo>();
		MenuVo m = null;
		for (Tmenu tmenu : tmenus) {
			m = new MenuVo();
			m.setId(tmenu.id);
			m.setIconcls(tmenu.iconcls);
			m.setName(tmenu.text);
			m.setUrl(tmenu.url);
			if (tmenu.PID != null) {
				Tmenu t = Tmenu.findById(tmenu.PID);
				if (t != null) {
					m.setPid(t.id);
					m.setPname(t.text);
				}
			}
			menus.add(m);
		}
		return menus;
	}

	@POST
	@Path("add")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public JsonVO<Tmenu> add(@FormParam("pid") String pid, @FormParam("id") String id, @FormParam("name") String name,
			@FormParam("url") String url) {
		Integer pid2 = StringUtils.isBlank(pid) ? null : Integer.valueOf(pid);
		Tmenu tmenu = null;
		if (StringUtils.isBlank(id)) {
			tmenu = new Tmenu();
			tmenu.text = name;
			tmenu.url = url;
			tmenu.PID = pid2;
			tmenu.persist();
		} else {
			tmenu = Tmenu.findById(Integer.valueOf(id));
			if (tmenu != null) {
				tmenu.text = name;
				tmenu.url = url;
				tmenu.PID = pid2;
			}
		}
		JsonVO<Tmenu> j = new JsonVO<>();
		j.setMsg("添加或修改成功");
		j.setSuccess(true);
		j.setData(tmenu);
		return j;
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
		Tmenu tmenu = Tmenu.findById(id);
		return menuEdit.data("tmenu", tmenu);
	}

	@DELETE
	@Path("delete/{id}")
	@Transactional
	public JsonVO<Object> delete(@PathParam("id") Integer id) {
		JsonVO<Object> j = new JsonVO<>();
		try {
			Tmenu.deleteById(id);
			j.setMsg("删除成功");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return j;
	}
}
