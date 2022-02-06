package org.acme;

import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;

/**
 * 消费
 * 
 * @author weir
 *
 */
@ApplicationScoped
public class UserInfoProcessor {

	@Incoming("userinfo-msg")
//    @Blocking
	public CompletionStage<Void> process(Message<String> msg) throws InterruptedException {
		Thread.sleep(200);
		String userInfo = msg.getPayload();
		System.out.println("msg-----" + userInfo);
		// if (userInfo.contains("weir10")) {
		// 	return msg.nack(new Throwable("error------" + userInfo));
		// } else {
			// }
				return msg.ack();
	}

	@Incoming("userinfo-requests")
	@Blocking
	public void process(JsonObject p) throws InterruptedException {
		Thread.sleep(100);
		UserInfo userInfo = p.mapTo(UserInfo.class);
		System.out.println(userInfo);
	}

}