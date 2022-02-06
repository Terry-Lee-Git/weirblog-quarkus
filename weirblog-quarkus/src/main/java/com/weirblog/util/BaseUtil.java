package com.weirblog.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtil {

	/**
	 * 转换成(yyyy-MM-dd)
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(String ds) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (ds.indexOf("/")>0) {
			return sdf.parse(ds.replace("/", "-"));
		}else {
			return sdf.parse(ds);
		}
	}
	/**
	 * 转换成(yyyy-MM-dd)
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public static String parseDateToString(Date d) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}
	/**
	 * 转换成(yyyyMMdd)
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public static String parseDateToStringNo(Date d) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(d);
	}
	/**
	 * 转换成(yyyyMMddHHmmss)
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public static String parseDateToStringNomm(Date d) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(d);
	}
	/**
	 * 获取年份
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public static String year() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yy");
		return sdf.format(new Date());
	}
	/**
	 * 转换成(yyyy-MM-dd HH:mm:ss)
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public static String parseDateToStringss(Date d) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	
	/**
	 * 转换成(yyyy-MM-dd HH:mm)
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public static Date parseDateTime(String ds) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (ds.indexOf("/")>0) {
			return sdf.parse(ds.replace("/", "-"));
		}else {
			return sdf.parse(ds);
		}
	}
	/**
	 * 转换成(yyyy-MM-dd HH:mm:ss)
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public static Date parseTimeStamp(String ds) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (ds.indexOf("/")>0) {
			return sdf.parse(ds.replace("/", "-"));
		}else {
			return sdf.parse(ds);
		}
	}
	
	public static boolean isEmpty(String str) {
		if (str!=null && !"".equals(str.trim())) {
			return true;
		}else {
			return false;
		}
	}
	public static boolean isEmpty(Long str) {
        if (str!=null && str>0) {
			return true;
		}else {
			return false;
		}
    }
	public static boolean isEmpty(Short str) {
        if (str!=null && str>0) {
			return true;
		}else {
			return false;
		}
    }
	public static boolean isEmpty(Double str) {
        if (str!=null && str>0) {
			return true;
		}else {
			return false;
		}
    }
	public static boolean isEmpty(Integer str) {
        if (str!=null && str>0) {
			return true;
		}else {
			return false;
		}
    }
	public static boolean isEmptyDate(Date str) {
        if (str!=null && !"".equals(str)) {
			return true;
		}else {
			return false;
		}
    }
	public static boolean isEmptySub(String[] str) {
        if (str!=null && str.length>0) {
			return true;
		}else {
			return false;
		}
    }
	public static boolean isEmptySub(Integer[] str) {
        if (str!=null && str.length>0) {
			return true;
		}else {
			return false;
		}
    }
	public static boolean isEmptySub(Short[] str) {
		if (str!=null && str.length>0) {
			return true;
		}else {
			return false;
		}
	}
	public static boolean isEmptySub(Long[] str) {
        if (str!=null && str.length>0) {
			return true;
		}else {
			return false;
		}
    }
	@SuppressWarnings("rawtypes")
	public static boolean isEmptyList(List str) {
		if (str!=null && str.size()>0) {
			return true;
		}else {
			return false;
		}
	}
	public static Double stringToDouble(String str) {
		if (str!=null && !"".equals(str.trim())) {
			return Double.valueOf(str);
		}else {
			return Double.valueOf(0);
		}
	}
	public static Double stringToDouble(Object str) {
		if (str!=null && !"".equals(str)) {
			return Double.valueOf((String) str);
		}else {
			return Double.valueOf(0);
		}
	}
	public final static Double exitAndDouble(Double d) {
		if (d!=null && d>0) {
			return d;
		}else {
			return Double.valueOf(0);
		}
	}
	public static Integer exitAndint(Integer d) {
		if (d!=null && d>0) {
			return d;
		}else {
			return Integer.valueOf(0);
		}
	}
	public static Date exitAndDate(Date d) {
		if (d!=null && !"".equals(d)) {
			return d;
		}else {
			return new Date();
		}
	}
	/**
	 * 不区分大小写
	 * @param str
	 * @return
	 */
	public static String convertString(String str){
		String upStr = str.toUpperCase();
		String lowStr = str.toLowerCase();
		StringBuffer buf = new StringBuffer(str.length());
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == upStr.charAt(i)) {
				buf.append(lowStr.charAt(i));
			} else {
				buf.append(lowStr.charAt(i));
			}
		}
		return buf.toString();
	}
	
	/**
	 * 转换为大写
	 * @param src
	 * @return
	 */
	public static String convertString1(String src) {
		char[] array = src.toCharArray();
		int temp = 0;
		for (int i = 0; i < array.length; i++) {
			temp = (int) array[i];
			if (temp <= 122 && temp >= 97) {
				array[i] = (char) (temp - 32);
			}
		}
		return String.valueOf(array);
	}
	/**
	 * double转换成String
	 * @param str
	 * @return
	 */
	public static String doubleToString(Double str) {
		DecimalFormat df = new DecimalFormat();
		return df.format(str);
	}
	/**
	 * 数字转换成字母
	 * @param i
	 * @return
	 */
	public static String getCha(Short i) {
		Character character = (char)(i+64);
		return character.toString();
	}
	
	public static String getStringByDouble(Double d) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(d);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(parseDateToStringNo(new Date()));
	}
	
	private static final String[] beforeShuffle = new String[] { "2", "3", "4", "5", "6", "7",  
        "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",  
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
        "W", "X", "Y", "Z" };
	public static String random() {
	     List<String> list = Arrays.asList(beforeShuffle);  
	     Collections.shuffle(list);  
	     StringBuilder sb = new StringBuilder();  
	     for (int i = 0,h=list.size(); i < h; i++) {  
	         sb.append(list.get(i));  
	     }  
	     String afterShuffle = sb.toString();  
	     String result = afterShuffle.substring(5, 9);
	     return result;
	}
	
	/**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
}
