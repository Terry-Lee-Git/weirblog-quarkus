package com.weirblog.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.weirblog.entity.Tmenu;
import com.weirblog.repository.TmenuRepository;
import com.weirblog.vo.MenuVo;

//import io.quarkus.cache.CacheKey;
//import io.quarkus.cache.CacheResult;


@ApplicationScoped
public class TmenuService {

	@Inject
	TmenuRepository tmenuRepository;
	
//	@CacheResult(cacheName = "tree-cache")
	public List<MenuVo> tree(Integer id) {
		List<Tmenu> tmenus = null;
		if (id.intValue() == 0) {
			tmenus = tmenuRepository.list("PID is null");
		} else {
			tmenus = tmenuRepository.list("PID = ?1", id);
		}
		List<MenuVo> menus = new ArrayList<MenuVo>();
		MenuVo m = null;
		for (Tmenu tmenu : tmenus) {
			m = new MenuVo();
			m.setId(tmenu.id);
			m.setText(tmenu.text);
			m.setName(tmenu.text);
			m.setIconcls(tmenu.iconcls);
			m.setUrl(tmenu.url);
			m.setPid(tmenu.PID != null ? tmenu.PID : null);
			List<Tmenu> tmenus2 = tmenuRepository.list("PID = ?1", tmenu.id);
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("url", tmenu.url);
			m.setAttributes(attributes);
			if (tmenus2 != null && !tmenus2.isEmpty()) {
				m.setState("closed");
			} else {
				m.setState("open");
			}
			menus.add(m);
		}
		return menus;
	}
}
