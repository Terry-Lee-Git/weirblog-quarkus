package com.weirblog.entity;

import java.util.Date;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.weirblog.vo.BaseVo;

/**
 * 评论
 * @author Administrator
 *
 */
@Entity
public class Comments extends BaseVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	/**
	 * 文章ID
	 */
	public Integer postsId;
	public Integer userId;
	public String name;
	public String email;
	public String IP;
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
	public Date createDate = new Date();
	public String content;
	public String approved="1";
	public String profileImageUrl;
}
