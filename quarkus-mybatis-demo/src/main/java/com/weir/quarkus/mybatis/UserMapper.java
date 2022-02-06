package com.weir.quarkus.mybatis;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface UserMapper extends BaseMapper<Users> {

    @Select("SELECT * FROM USERS WHERE id = #{id}")
    Users getUser(Integer id); 

    @Insert("INSERT INTO USERS (id, name) VALUES (#{id}, #{name})")
    Integer createUser(@Param("id") Integer id, @Param("name") String name); 

    @Delete("DELETE FROM USERS WHERE id = #{id}")
    Integer removeUser(Integer id); 
}