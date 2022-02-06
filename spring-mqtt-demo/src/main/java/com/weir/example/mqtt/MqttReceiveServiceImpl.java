package com.weir.example.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.weir.example.common.StaticStringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息接收实现
 * 
 * @author weir
 *
 *         2019年6月19日 上午9:58:41
 */
@Slf4j
@Service
public class MqttReceiveServiceImpl implements MqttReceiveService {

//	@Autowired
//	private IYakenTopicDataService yakenTopicDataService;
//	@Autowired
//	private IYakenCommandRevertRecordService yakenCommandRevertRecordService;
//	@Autowired
//	private YakenAnalysisInfoTasksService yakenAnalysisInfoTasksService;

	@Value("${mqtt.dataIntervalTime}")
	private int dataIntervalTime;

	@Async
	@Override
	public void handlerMqttMessage(String topic, String jsonData) {
		log.info("this topic is ： " + topic + " ,this data : " + jsonData);
//		if (topic.contains(StaticStringUtil.DATA)) { // 实时数据
//			log.info("to-save-data:" + jsonData);
//			long cacheTime = yakenAnalysisInfoTasksService.cacheTime(topic);
//			if (checkSaveData(cacheTime, dataIntervalTime)) {
//				log.info("to-save-data:" + jsonData);
//				yakenAnalysisInfoTasksService.clearCache(topic);
//				yakenTopicDataService.addData(topic, jsonData);
//			}
//		} else if (topic.contains(StaticStringUtil.CONTROL_REPLY)) { // 变量控制回复
//			yakenCommandRevertRecordService.add(topic, jsonData);
//		} else if (topic.contains(StaticStringUtil.COLLECTOR_INFO)) { // 网关信息
//			yakenCommandRevertRecordService.addCollectorInfoBack(topic, jsonData);
//		} else {
////			yakenTopicDataService.add(topic, jsonData);
//		}
	}

	private static boolean checkSaveData(long cacheTime, int dataIntervalTime) {
		long ct = cacheTime + dataIntervalTime;
		return (System.currentTimeMillis() - ct) >= 0 ? true : false;
	}

}
