package com.weir.example.demo;

public interface TestMqttService {

	void initAddTopic(Integer h, String prefix);

	void initRemoveTopic(Integer h, String prefix);

	void initSendData(Integer h, String prefix, String data);

}
