package com.open.framework.commmon.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 *
 *功能说明:操作字符串的工具类
 */
public class StringUtil extends StringUtils {
	/**
	 * 下划线转驼峰
	 * @param param
	 * @return
	 */
	public static String underlineToCaml(String param) {
		if (isEmpty(param)) {
			return "";
		}
		int len = param.length();
		boolean camlFlag = false;
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if("".equals(c)){
				continue;
			}
			else if(String.valueOf(c).equals("_")){
				camlFlag = true;
				continue;
			}
			if(camlFlag){
				sb.append(Character.toUpperCase(c));
				camlFlag = false;
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 驼峰转下划线
	 * @param param
	 * @return
	 */
	public static String camelToUnderline(String param) {
		if (isEmpty(param)) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				sb.append('_');
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}

	/**
	 * 根据字符拆分字符串返回list
	 * @param str
	 * @param separatorChars
	 * @return
	 */
	public static List<String> getSplitList(String str,String separatorChars){
		String[] aa=split(str,separatorChars);
		return Arrays.asList(aa);
	}

	public static String isEmptyDefault(String value,String defaultVal){
		return isNotEmpty(value)?value:defaultVal;
	}
}
