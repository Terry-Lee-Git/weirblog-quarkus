package com.weirblog.entity;

import java.util.Date;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.weirblog.vo.BaseVo;

/**
 * 
 * @ClassName: Video 
 * @Description: 视频
 * @author 钮炜炜
 * @date 2015年10月6日 下午3:04:33 
 *
 */
@Entity
public class Video extends BaseVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	public String vname;
	public String vpath;
	public String vurl;
	public String vdesc;
	public String thumbnail;
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
	public Date createDate = new Date();
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
	public Date updateDate;
}
