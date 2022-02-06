package com.weir.quarkus.mybatis;


import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

public class Users extends Model<Users> {
	public Integer id;
    public Date createDate;
    public String email;
    public String userName;
    public String userPwd;
    public String idStr;
    public String profileImageUrl;

}
