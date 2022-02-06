package com.weir.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weir.example.common.ApiRespResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "性能测试")
@RestController
@RequestMapping("/weir-test")
public class TestMqttController {

	@Autowired
	private TestMqttService testMqttService;
	
	@Operation(summary = "初始化topic")
	@GetMapping("init-add-topic/{h}/{prefix}")
	public ApiRespResult<Object> initAddTopic(@PathVariable("h") Integer h, @PathVariable("prefix") String prefix) {
		testMqttService.initAddTopic(h, prefix);
		return new ApiRespResult<>();
	}
	@Operation(summary = "删除topic")
	@GetMapping("init-remove-topic/{h}/{prefix}")
	public ApiRespResult<Object> initRemoveTopic(@PathVariable("h") Integer h, @PathVariable("prefix") String prefix) {
		testMqttService.initRemoveTopic(h, prefix);
		return new ApiRespResult<>();
	}
	@Operation(summary = "生产数据")
	@GetMapping("init-send-topic/{h}/{prefix}")
	public ApiRespResult<Object> initRemoveTopic(@PathVariable("h") Integer h, @PathVariable("prefix") String prefix, String data) {
		testMqttService.initSendData(h, prefix, data);
		return new ApiRespResult<>();
	}
}
