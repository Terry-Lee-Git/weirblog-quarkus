package com.weirblog.entity;

import java.util.Date;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.weirblog.vo.BaseVo;

/**
 * 相册
 * @author Administrator
 *
 */
@Entity
public class Photo extends BaseVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	public String name;
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
	public Date createDate = new Date();
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
	public Date updateDate;
	public String thumbnail;
	public Photo() {
		super();
	}
	public Photo(Integer id) {
		super();
		this.id = id;
	}
}
