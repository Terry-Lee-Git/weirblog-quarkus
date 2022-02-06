package com.weir.example.common;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Json工具类
 * @author weir
 *
 * 2019年6月18日 上午9:53:37
 */
@Slf4j
public class JsonMapper {

    private ObjectMapper mapper;

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(Include include) {
        mapper = new ObjectMapper();
        // 设置输出时包含属性的风格
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 序列化
     * @param object obj
     * @return json
     */
    public String dumps(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            log.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化
     * @param json json
     * @param type 目标对象类型
     * @param <T> 泛型类型
     * @return 对象
     */
    public <T> T loads(String json, TypeReference<T> type) {
        if (StringUtils.hasText(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
        	log.error("JsonMapper--loads--:" + json, e);
        }
        return null;
    }

    /**
     * 反序列化
     * @param json json
     * @param clazz 目标对象类型
     * @param <T> 泛型类型
     * @return 对象
     */
    public <T> T loads(String json, Class<T> clazz) {
        if (StringUtils.hasText(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
        	log.warn("parse json string error:" + json, e);
        }
        return null;
    }

    /**
     * @param object object
     * @param clazz clazz
     * @param  <T> <T>
     * @return t
     */
    public <T> T converts(Object object, Class<T> clazz) {
    	if (object == null) {
			return null;
		}
		try {
			return mapper.convertValue(object, clazz);
		} catch (IllegalArgumentException e) {
			log.warn("parse object Object error:" + object, e);
		}
		return null;
	}

    public ObjectMapper getMapper() {
        return mapper;
    }
    
    public static void main(String[] args) {
		String ss = "{\"time\":\"1560236860890\",\"value\":{\"0\":10,\"1\":120000,\"2\":10000,\"3\":600,\"4\":9000,\"5\":90000,\"6\":12,\"7\":130000,\"8\":11000,\"9\":990,\"10\":9100,\"11\":80000,\"12\":0,\"13\":0,\"14\":1,\"15\":1,\"16\":0,\"17\":0,\"18\":0,\"19\":0,\"20\":0,\"21\":0,\"22\":0,\"23\":0,\"24\":0,\"25\":0,\"26\":80,\"27\":60,\"28\":70,\"29\":90,\"30\":14,\"31\":16,\"32\":17,\"33\":19,\"34\":-50,\"35\":-45,\"36\":70,\"37\":80,\"38\":90,\"39\":100,\"40\":1,\"41\":0}}";
		JsonMapper jsonMapper = new JsonMapper();
		Map map = jsonMapper.loads(ss, Map.class);
		LinkedHashMap<Integer, Object> linkedHashMap = (LinkedHashMap<Integer, Object>) map.get(StaticStringUtil.VALUE);
		System.out.println(linkedHashMap);
	}
}
