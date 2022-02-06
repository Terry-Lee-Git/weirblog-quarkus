package com.weirblog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.weirblog.vo.BaseVo;

import java.util.Date;

@Entity
public class Users extends BaseVo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
    public Date createDate;
    public String email;
    public String userName;
    public String userPwd;
    public String idStr;
    public String profileImageUrl;

}
