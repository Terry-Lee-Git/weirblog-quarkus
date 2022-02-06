package com.weirblog.util;

import io.vertx.core.http.HttpServerRequest;

/**
 * IP工具类
 * 
 */
public class IpUtil {
	
//	public static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 获取登录用户的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServerRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.host();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "本地";
		}
		if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

	/**
	 * 通过IP获取地址(需要联网，调用淘宝的IP库)
	 * 
	 * @param ip
	 * @return
	 */
//	public static String getIpInfo(String ip) {
//		if (ip.equals("本地")) {
//			ip = "127.0.0.1";
//		}
//		String info = "";
//		try {
//			URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
//			HttpURLConnection htpcon = (HttpURLConnection) url.openConnection();
//			htpcon.setRequestMethod("GET");
//			htpcon.setDoOutput(true);
//			htpcon.setDoInput(true);
//			htpcon.setUseCaches(false);
//
//			InputStream in = htpcon.getInputStream();
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//			StringBuffer temp = new StringBuffer();
//			String line = bufferedReader.readLine();
//			while (line != null) {
//				temp.append(line).append("\r\n");
//				line = bufferedReader.readLine();
//			}
//			bufferedReader.close();
////			JSONObject obj = (JSONObject) JSON.parse(temp.toString());
//			JsonVO<IpInfoVO> jsonVO = mapper.readValue(temp.toString(), JsonVO.class);
////			Map readValue = mapper.readValue(temp.toString(), Map.class);
//			System.out.println(jsonVO.toString());
////			if (obj.getIntValue("code") == 0) {
////				JSONObject data = obj.getJSONObject("data");
////				info += data.getString("country") + " ";
////				info += data.getString("region") + " ";
////				info += data.getString("city") + " ";
////				info += data.getString("isp");
////			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return info;
//	}

	public static void main(String[] args) {
//		IpUtil ipUtil = new IpUtil();
//		System.out.println(ipUtil.getIpInfo("125.71.133.173"));
//		System.out.println(ipUtil.getIpInfo("113.83.67.116"));
	}
}
