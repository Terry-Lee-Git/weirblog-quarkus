//package com.weirblog.resource.redis;
//
//import java.util.Arrays;
//
//import javax.inject.Inject;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//
//import io.quarkus.redis.client.RedisClient;
//
//@Path("/redis")
//public class RedisTestResource {
//
//	@Inject
//    RedisClient redisClient;
//	
//	@POST
//	public StringDTO add(StringDTO dto) {
//		redisClient.set(Arrays.asList(dto.key, dto.value));
//		return dto;
//	}
//	
//	@GET()
//	@Path("{key}")
//	public String get(@PathParam(value = "key") String key) {
//		return redisClient.get(key).toString();
//	}
//}
