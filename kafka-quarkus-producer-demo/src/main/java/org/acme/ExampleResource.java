package org.acme;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

	/**
	 * 注入响应式消息Emitter以向userinfo-requests通道发送消息。
	 * 配置
	 * mp.messaging.outgoing.userinfo-requests.connector=smallrye-kafka
## 自己实现的如果是对象就需要序列化
mp.messaging.outgoing.userinfo-requests.value.serializer=io.quarkus.kafka.client.serialization.ObjectMapperSerializer
	 */
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
    		userInfoRequestEmitter.send(userInfo); // 发送
		}
        return new UserInfo(UUID.randomUUID().toString(), "weir");
    }

}