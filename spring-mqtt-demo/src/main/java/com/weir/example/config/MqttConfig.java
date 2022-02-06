package com.weir.example.config;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.weir.example.mqtt.MqttReceiveService;

import lombok.extern.slf4j.Slf4j;

/**
 * MQTT接收消息处理
 * 
 * @author weir
 *
 *         2019年6月16日 下午2:41:45
 */
@Slf4j
@Configuration
@IntegrationComponentScan
public class MqttConfig {

	private static final String MQTT_RECEIVEDTOPIC = "mqtt_receivedTopic";
	private static final String SP_INBOUND = "_inbound";

	private MqttPahoMessageDrivenChannelAdapter adapter;

	@Value("${mqtt.username}")
	private String username;

	@Value("${mqtt.pwd}")
	private String password;

	@Value("${mqtt.url}")
	private String hostUrl;

	@Value("${mqtt.client.id}")
	private String clientId;

	@Value("${mqtt.default.topic}")
	private String defaultTopic;

	@Value("${mqtt.completionTimeout}")
	private int completionTimeout;

	@Autowired
	private MqttReceiveService mqttReceiveService;

	@Bean
	public MqttConnectOptions getMqttConnectOptions() {
		MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
		mqttConnectOptions.setUserName(username);
		mqttConnectOptions.setPassword(password.toCharArray());
		mqttConnectOptions.setServerURIs(new String[] { hostUrl });
		mqttConnectOptions.setKeepAliveInterval(2); // 设置会话心跳时间
		return mqttConnectOptions;
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(getMqttConnectOptions());
		return factory;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(defaultTopic);
		return messageHandler;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	/**
	 * 接收通道
	 * 
	 * @return MessageChannel
	 */
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	/**
	 * 配置client,监听的topic
	 * 
	 * @return MessageProducer
	 */
	@Bean
	public MessageProducer inbound() {
		adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + SP_INBOUND, mqttClientFactory(), defaultTopic);
		adapter.setCompletionTimeout(completionTimeout);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setOutputChannel(mqttInputChannel());
		log.info("--------------------inbound------------------");
		return adapter;
	}

	/**
	 * 通过通道获取数据
	 * 
	 * @return MessageHandler
	 */
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {
			@Override
			public void handleMessage(final Message<?> message) throws MessagingException {
				final String topic = message.getHeaders().get(MQTT_RECEIVEDTOPIC).toString();
				mqttReceiveService.handlerMqttMessage(topic, message.getPayload().toString());
			}
		};
	}

	/**
	 * 动态添加主题
	 * 
	 * @param topicArr 数组
	 */
	public void addListenTopic(String[] topicArr) {
		if (topicArr == null || topicArr.length == 0) {
			return;
		}
		if (adapter == null) {
			adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + SP_INBOUND, mqttClientFactory(), "");
		}
		for (String topic : topicArr) {
			try {
				if (!Arrays.asList(adapter.getTopic()).contains(topic)) {					
					adapter.addTopic(topic, 0);
				}
			} catch (Exception e) {
				log.error("MqttConfig---addListenTopic--[]-{}", e);
			}
		}
	}

	/**
	 * 动态添加主题
	 * 
	 * @param topic 主题
	 */
	public void addListenTopic(String topic) {
		if (StringUtils.isNotBlank(topic)) {
			if (adapter == null) {
				adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + SP_INBOUND, mqttClientFactory(), "");
			}
			try {
				if (!Arrays.asList(adapter.getTopic()).contains(topic)) {					
					adapter.addTopic(topic, 0);
				}
			} catch (Exception e) {
				log.error("MqttConfig---addListenTopic---{}", e);
			}
		}
	}

	/**
	 * 删除单个主题
	 * 
	 * @param topic 主题
	 */
	public void removeListenTopic(String topic) {
		if (adapter == null) {
			adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + SP_INBOUND, mqttClientFactory(), "");
		}
		try {
			adapter.removeTopic(topic);
		} catch (Exception e) {
			log.error("MqttConfig---removeListenTopic---{}", e);
		}
	}
}
