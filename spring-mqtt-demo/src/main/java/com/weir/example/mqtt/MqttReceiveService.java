package com.weir.example.mqtt;
import org.springframework.stereotype.Service;

/**
 * @author weir
 *
 * 2019年6月19日 上午9:57:21
 */
@Service
public interface MqttReceiveService {
 
    /**
     * 主题数据接收
     * @param topic 主题
     * @param jsonData 数据
     */
    void handlerMqttMessage(String topic,String jsonData);
}
