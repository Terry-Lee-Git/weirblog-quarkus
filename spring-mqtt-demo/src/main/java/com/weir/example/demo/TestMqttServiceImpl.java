package com.weir.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weir.example.common.StaticStringUtil;


@Service
public class TestMqttServiceImpl implements TestMqttService {

	@Autowired
	private IMqttConfigService yakenMqttConfigService;
	
	@Override
	public void initAddTopic(Integer h,String prefix) {
		for (int i = 0; i < h; i++) {			
			yakenMqttConfigService.listenTopic(prefix + i, StaticStringUtil.DATA);
		}
	}
	
	@Override
	public void initRemoveTopic(Integer h,String prefix) {
		for (int i = 0; i < h; i++) {			
			yakenMqttConfigService.removeListenTopic(prefix + i);
		}
	}
//	/dtu/test-weir54/data
	private static final String topic = "/dtu/%s/data";
	@Override
	public void initSendData(Integer h,String prefix, String data) {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < h; j++) {				
				yakenMqttConfigService.sendToMqtt(data + i, String.format(topic, prefix + i));
			}
		}
	}
	
	
}
