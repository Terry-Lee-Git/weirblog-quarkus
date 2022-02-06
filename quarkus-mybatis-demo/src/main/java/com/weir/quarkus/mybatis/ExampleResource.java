package com.weir.quarkus.mybatis;

import javax.cache.annotation.CachePut;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Path("/hello")
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }
    
    @Inject
    UserMapper userMapper; 

    @CachePut(cacheName = "users")
    @Path("/user/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Users getUser(@PathParam("id") Integer id) {
        return userMapper.getUser(id);
    }

    @Path("/user")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Integer createUser(@FormParam("id") Integer id, @FormParam("name") String name) {
        return userMapper.createUser(id, name);
    }

    @Path("/user/{id}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Integer removeUser(@PathParam("id") Integer id) {
        return userMapper.removeUser(id);
    }
    
    @Path("/user/page/{page}/{pageSize}")
    @GET
    public Page<Users> list(@PathParam("page") Integer page,@PathParam("pageSize") Integer pageSize) {
		return userMapper.selectPage(new Page<>(page, pageSize), null);
	}
}