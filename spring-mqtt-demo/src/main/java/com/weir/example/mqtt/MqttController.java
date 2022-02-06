package com.weir.example.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.weir.example.common.ApiRespResult;
import com.weir.example.config.MqttConfig;


//@Api(tags = "MQTT发布订阅测试管理")
@RestController
public class MqttController {
	@Autowired
    private MqttGateway mqttGateway;
	@Autowired
	private MqttConfig mqttConfig;
 
//	@ApiOperation("发送消息")
    @GetMapping("/send_mqtt/{topic}")
    public ApiRespResult<Object> sendMqtt(String  sendData, @PathVariable String topic){
        mqttGateway.sendToMqtt(sendData,topic);
        return ApiRespResult.ok();
    }
	
//	@ApiOperation("注册监听主题")
	@GetMapping("/add_topic")
	public ApiRespResult<Object> addTopic(String[] topic) {
		mqttConfig.addListenTopic(topic);
		return ApiRespResult.ok();
	}
	
//	@ApiOperation("删除监听主题")
	@GetMapping("/delete")
	public ApiRespResult<Object> delete(String topic) {
		mqttConfig.removeListenTopic(topic);
		return ApiRespResult.ok();
	}
}
