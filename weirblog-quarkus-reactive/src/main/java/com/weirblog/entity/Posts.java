package com.weirblog.entity;

import java.util.Date;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.weirblog.vo.BaseVo;

import io.quarkus.qute.TemplateData;

/**
 * 文章表
 */
@TemplateData
@Entity
//@Table(name = "posts")
public class Posts extends BaseVo {

	@Id
	public Integer id;
	/**
	 * 首图
	 */
	public String postPic;
	public String postTitle;
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
	public Date createDate = new Date();
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
	public Date updateDate;
	public Integer userId;
	public String types;
	public String description;
	/**
	 * 阅读量
	 */
	public Integer readNum = 0;
	/**
	 * 评论量
	 */
	public Integer commNum = 0;
	
//	@JsonbProperty(nillable=true)
//	@Basic(fetch=FetchType.LAZY)
	@Lob
	public String content;

	public Posts() {
	}

	public Posts(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Posts [id=" + id + ", postTitle=" + postTitle + ", createDate=" + createDate + ", updateDate="
				+ updateDate + ", userId=" + userId + ", types=" + types + ", description=" + description + ", readNum="
				+ readNum + ", commNum=" + commNum + ", content=" + content + "]";
	}

}