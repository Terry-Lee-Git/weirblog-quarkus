package com.weirblog.resource.template;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@ApplicationScoped
@Path("admin/index")
@Produces(MediaType.TEXT_HTML)
public class AdminIndexResource {

	@Location("admin/north.html") 
    Template north;
    @Location("admin/south.html") 
    Template south;
    @Location("admin/welcome.html") 
    Template welcome;
    @Location("admin/main.html") 
    Template main;
    
    @Location("admin/menu.html") 
    Template menu;
    
    @Location("admin/user.html") 
    Template user;
    @Location("admin/post.html") 
    Template post;
    @Location("admin/photo.html") 
    Template photo;
    @Location("admin/video.html") 
    Template video;
    @Location("admin/comments.html") 
    Template comments;
    
    @GET
    public TemplateInstance getMain() {
        return main.data(null);  
    }
    @GET
    @Path("north")
    public TemplateInstance getNorth() {
    	return north.data(null);
    }
    @GET
    @Path("south")
    public TemplateInstance getSouth() {
    	return south.data(null);
    }
    @GET
    @Path("welcome")
    public TemplateInstance getWelcom() {
    	return welcome.data(null);
    }
    
	
	@GET
    @Path("menu")
    public TemplateInstance menu() {
        return menu.data(null);  
    }
	
	@GET
	@Path("user")
	public TemplateInstance user() {
		return user.data(null);  
	}
	@GET
	@Path("post")
	public TemplateInstance post() {
		return post.data(null);  
	}
	@GET
	@Path("photo")
	public TemplateInstance photo() {
		return photo.data(null);  
	}
	@GET
	@Path("video")
	public TemplateInstance video() {
		return video.data(null);  
	}
	@GET
	@Path("comments")
	public TemplateInstance comments() {
		return comments.data(null);  
	}
	
	
	@ConfigProperty(name = "photo.img.save-path")
	String photoImgPath;
	
	@GET
    @Path("/img/{name}")
    public Response getAvatar(@PathParam("name") String name) {
        final File file = new File(photoImgPath + name);
        return Response.ok(file).cacheControl(new CacheControl()).build();
    }
}
