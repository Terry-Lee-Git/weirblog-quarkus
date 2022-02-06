package com.weir.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weir.example.common.JsonMapper;
import com.weir.example.common.MqttTimeCalibrationVo;
import com.weir.example.common.StaticStringUtil;
import com.weir.example.config.MqttConfig;
import com.weir.example.mqtt.MqttGateway;

/**
 * <p>
 * MQTT消息配置 服务实现类
 * </p>
 *
 * @author weir
 * @since 2019-06-04
 */
@Service
public class YakenMqttConfigServiceImpl implements IMqttConfigService {

	@Autowired
	private MqttGateway mqttGateway;
	@Autowired
	private MqttConfig mqttConfig;
	
	private JsonMapper jsonMapper = new JsonMapper();
	
	
	@Override
	public void listenTopic(String clientId, String suffix) {
		String topic = null;
		switch (suffix) {
		case StaticStringUtil.DATA:
			topic = String.format(StaticStringUtil.TOPIC_MQTT, clientId);
			timeCalibration(String.format(StaticStringUtil.TOPIC_MQTT_TIME, clientId));
			mqttConfig.addListenTopic(topic);
			break;
		case StaticStringUtil.CONTROL_REPLY:
			topic = String.format(StaticStringUtil.TOPIC_MQTT_CONTROLREPLY, clientId);
			mqttConfig.addListenTopic(topic);
			break;
		case StaticStringUtil.TIME:
			topic = String.format(StaticStringUtil.TOPIC_MQTT_TIME, clientId);
			timeCalibration(topic);
			break;
		case StaticStringUtil.COLLECTOR_INFO:
			topic = String.format(StaticStringUtil.TOPIC_MQTT_COLLECTORINFO, clientId);
			mqttConfig.addListenTopic(topic);
			break;
		default:
			break;
		}
	}
	
	private void timeCalibration(String topic) {
		MqttTimeCalibrationVo timeCalibrationVo = new MqttTimeCalibrationVo();
		timeCalibrationVo.setTime(String.valueOf(System.currentTimeMillis()));
		mqttGateway.sendToMqtt(jsonMapper.dumps(timeCalibrationVo), topic);
	}
	
	private static final String MSG_TO_CUSTOMER_STRING = "设备%s消息";
	
	
	@Override
	public void removeListenTopic(String topic) {
		mqttConfig.removeListenTopic(topic);
	}
	
	@Override
	public void sendToMqtt(String data, String topic) {
		mqttGateway.sendToMqtt(data, topic);
	}
}
