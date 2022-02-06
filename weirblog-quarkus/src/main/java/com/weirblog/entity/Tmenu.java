package com.weirblog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.weirblog.vo.BaseVo;

/**
 * 菜单资源
 *     
 */
@Entity
public class Tmenu extends BaseVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	public String text;
	public String iconcls;
	public String url;
	
	public Integer PID;

}