package com.weir.example.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 时间校准
 * @author weir
 *
 * 2019年8月1日 下午1:45:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MqttTimeCalibrationVo {

	private String time;
}