package com.weirblog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.weirblog.vo.BaseVo;

/**
 * 相册图片
 * @author weir
 *
 */
@Entity
public class PhotoImg extends BaseVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	public String imgName;
	public String imgPath;
	public String imgDesc;
	public String thumbnail;
	public Integer photoId;
	
}
