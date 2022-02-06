package com.weir.example.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.integration.mqtt.support.MqttHeaders;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {
	void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
}
