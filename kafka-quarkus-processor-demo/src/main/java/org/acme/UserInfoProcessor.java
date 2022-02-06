package org.acme;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaCloudEventMetadata;
import io.smallrye.reactive.messaging.kafka.Record;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 消费
 * @author weir
 *
 */
@ApplicationScoped
public class UserInfoProcessor {
/**
 * mp.messaging.incoming.userInfo-requests.connector=smallrye-kafka
mp.messaging.incoming.userInfo-requests.topic=userinfo-requests // topic
mp.messaging.incoming.userInfo-requests.auto.offset.reset=earliest


mp.messaging.outgoing.userInfos.connector=smallrye-kafka
mp.messaging.outgoing.userInfos.value.serializer=io.quarkus.kafka.client.serialization.ObjectMapperSerializer
 * @param userInfoRequest
 * @return
 * @throws InterruptedException
 */
//	@Incoming("userInfo-requests") 
//    @Outgoing("userInfos")   
//    @Blocking             
//    public UserInfo process(String userInfoRequest) throws InterruptedException {
//        Thread.sleep(200);
//        System.out.println(userInfoRequest);
//        return new UserInfo(UUID.randomUUID().toString(), "weir");
//    }
	
	/**
	 * mp.messaging.incoming.userinfo-requests.connector=smallrye-kafka
       mp.messaging.incoming.userinfo-requests.auto.offset.reset=earliest
	 * @param userInfo
	 * @return
	 * @throws InterruptedException
	 */
//	@Incoming("userinfo-requests") 
//	@Blocking             
//	public CompletionStage<Void> processMsg(Message<UserInfo> userInfo) throws InterruptedException {
//		Thread.sleep(200);
////		IncomingKafkaCloudEventMetadata orElseThrow = userInfo.getMetadata(IncomingKafkaCloudEventMetadata.class).orElseThrow();
//		UserInfo payload = userInfo.getPayload();
//		System.out.println(payload);
//		return userInfo.ack(); //手动处理确认
//	}
	
//	@Incoming("userinfo-requests")
//	public void consume(ConsumerRecord<String, UserInfo> record) {
//	    String key = record.key(); // Can be `null` if the incoming record has no key
//	    UserInfo value = record.value(); // Can be `null` if the incoming record has no value
//	    String topic = record.topic();
//	    int partition = record.partition();
//	    System.out.println(key + "-" + value + "-" + topic + "-" + partition);
//	}
	
	

        /**
         * Acknowledgment managed by the user code. No automatic acknowledgment is performed. This strategy is only
         * supported by methods consuming {@link Message} instances.
         */
	//  由用户代码管理的确认。不执行自动确认。此策略仅由使用消息实例的方法支持。
//        MANUAL,

        /**
         * Acknowledgment performed automatically before the processing of the message by the user code.
         */
	// 在用户代码处理消息之前自动执行的确认。
//        PRE_PROCESSING,

        /**
         * Acknowledgment performed automatically once the message has been processed.
         * When {@code POST_PROCESSING} is used, the incoming message is acknowledged when the produced message is
         * acknowledged.
         *
         * Notice that this mode is not supported for all signatures. When supported, it's the default policy.
         *
         */
	// 消息处理后自动执行确认。
//        POST_PROCESSING,

        /**
         * No acknowledgment is performed, neither implicitly or explicitly.
         * It means that the incoming messages are going to be acknowledged in a different location or using a different
         * mechanism.
         */
//        NONE
    
	@Incoming("userinfo-requests")
//	@Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
	public void consume(Record<String, UserInfo> record) {
	    String key = record.key(); // Can be `null` if the incoming record has no key
	    UserInfo value = record.value(); // Can be `null` if the incoming record has no value
	    System.out.println(key + "-" + value );
	}
}