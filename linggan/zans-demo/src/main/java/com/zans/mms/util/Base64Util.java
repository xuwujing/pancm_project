package com.zans.mms.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author qitian
 * @Title: zans-demo
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/2
 */

public class Base64Util {

	/**
	 * base64 加密
	 *
	 * @param str
	 * @return
	 */
	public static String base64EnStr(String str) throws UnsupportedEncodingException {
		return  base64EnStr(str,null);
	}

	/**
	 * base64 加密
	 *
	 * @param str
	 * @return
	 */
	public static String base64EnStr(String str,String charsetName) throws UnsupportedEncodingException {
		if(StringUtils.isEmpty(charsetName)){
			charsetName = "UTF-8";
		}
		return Base64.getEncoder().encodeToString(str.getBytes(charsetName));
	}




	/**
	 * base64 加密
	 *
	 * @param str
	 * @return
	 */
	public static String base64DeStr(String str) throws UnsupportedEncodingException {
		return base64DeStr(str,null);

	}

	/**
	 * base64解密
	 *
	 * @param encodeStr
	 * @return
	 */
	public static String base64DeStr(String encodeStr,String charsetName) throws UnsupportedEncodingException {
		if(StringUtils.isEmpty(charsetName)){
			charsetName = "UTF-8";
		}
		byte[] decodeStr =  Base64.getDecoder().decode(encodeStr);
		return new String(decodeStr,charsetName);
	}
}
