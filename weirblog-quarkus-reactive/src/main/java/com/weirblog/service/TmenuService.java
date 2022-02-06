package com.weirblog.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.weirblog.entity.Tmenu;
import com.weirblog.repository.TmenuRepository;
import com.weirblog.vo.MenuVo;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;

//import io.quarkus.cache.CacheKey;
//import io.quarkus.cache.CacheResult;

@ApplicationScoped
public class TmenuService {

//	@Inject
//	TmenuRepository tmenuRepository;

//	@CacheResult(cacheName = "tree-cache")
	public void tree(Integer id) {
		
		Uni<List<Tmenu>> listAll = Tmenu.listAll();
		Uni<Map<Integer, Long>> pidMap = listAll.chain(list -> {
			Map<Integer, Long> map = list.parallelStream().filter(m -> m.getPID() != null)
					.collect(Collectors.groupingBy(Tmenu::getPID, Collectors.counting()));
			return Uni.createFrom().item(map);
		});
		
//		Uni<List<Tmenu>> tmenus = null;
//		if (id.intValue() == 0) {
//			tmenus = Tmenu.list("PID is null");
//		} else {
//			tmenus = Tmenu.list("PID = ?1", id);
//		}
//		final Uni<List<Tmenu>> menuList = tmenus;
//		return 
//				pidMap.chain(map -> menuList.chain(list -> {
//					List<MenuVo> menus = new ArrayList<MenuVo>();
//					for (Tmenu tmenu : list) {
//						MenuVo m = new MenuVo();
//						m.setId(tmenu.id);
//						m.setText(tmenu.text);
//						m.setName(tmenu.text);
//						m.setIconcls(tmenu.iconcls);
//						m.setUrl(tmenu.url);
//						m.setPid(tmenu.PID != null ? tmenu.PID : null);
//						Map<String, Object> attributes = new HashMap<String, Object>();
//						attributes.put("url", tmenu.url);
//						m.setAttributes(attributes);
//
//						Long count = map.get(tmenu.id);
//						if (count != null && count > 0) {
//							m.setState("closed");
//						} else {
//							m.setState("open");
//						}
//						menus.add(m);
//					}
//					return Uni.createFrom().item(menus);
//				})
//						);
		
		
				
//				tmenus.chain(list -> {
//			List<MenuVo> menus = new ArrayList<MenuVo>();
//			for (Tmenu tmenu : list) {
//				MenuVo m = new MenuVo();
//				m.setId(tmenu.id);
//				m.setText(tmenu.text);
//				m.setName(tmenu.text);
//				m.setIconcls(tmenu.iconcls);
//				m.setUrl(tmenu.url);
//				m.setPid(tmenu.PID != null ? tmenu.PID : null);
//				Map<String, Object> attributes = new HashMap<String, Object>();
//				attributes.put("url", tmenu.url);
//				m.setAttributes(attributes);
//
//				Tmenu.count("PID = ?1", tmenu.id)
//				.onItem().invoke((c)->{
//					if (c > 0) {
//						m.setState("closed");
//					} else {
//						m.setState("open");
//					}
//				});
//				menus.add(m);
//			}
//			return Uni.createFrom().item(menus);
//		}
//		
//				);
	}
}
