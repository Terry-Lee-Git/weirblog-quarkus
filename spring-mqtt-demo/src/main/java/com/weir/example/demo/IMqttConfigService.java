package com.weir.example.demo;

/**
 * <p>
 * MQTT消息配置 服务类
 * </p>
 *
 * @author weir
 * @since 2019-06-04
 */
public interface IMqttConfigService {


	void listenTopic(String clientId, String suffix);

	void removeListenTopic(String topic);

	void sendToMqtt(String data, String topic);

}
