package org.acme;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

    @Channel("userinfo-requests")
    Emitter<UserInfo> userInfoRequestEmitter;
    /**
     * 生产
     * @return
     */
    @POST
    @Path("/userinfo")
    @Produces(MediaType.TEXT_PLAIN)
    public UserInfo createRequest2() {
    	
    	for (int i = 0; i < 100; i++) {			
    		UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "weir");
    		userInfoRequestEmitter.send(userInfo); // 发送   io.vertx.core.json.JsonObject 
    		
		}
        return new UserInfo(UUID.randomUUID().toString(), "weir");
    }
    
    @Channel("userinfo-msg")
    Emitter<String> userInfoMsgEmitter;
    
    @POST
    @Path("/userinfo-msg")
    public UserInfo createRequest3() {
    	for (int i = 0; i < 100; i++) {			
    		UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "weir"+i);
    		userInfoMsgEmitter.send(Message.of(Json.encode(userInfo))); // 发送
    	}
    	return new UserInfo(UUID.randomUUID().toString(), "weir");
    }

}