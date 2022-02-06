package com.weirblog.util;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
/**
 * 判断客户端类型
 * @author weir
 *
 */
public class UserAgentUtil {

	/**
	 * 判断是否是移动端
	 * @param userAgent
	 * @return
	 */
	public static boolean checkMobile(String userAgent) {
		return UserAgent.parseUserAgentString(userAgent).getOperatingSystem().getDeviceType().equals(DeviceType.MOBILE);
	}
	
}
