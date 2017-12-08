package com.pancm.test.tokenTest;

/**
 * 
* Title: test
* Description: token 测试 
* Version:1.0.0  
* @author pancm
* @date 2017年10月27日
 */
public class test {
		/** Token 密钥 */
		private static String KEY = "qaz123";
		/** 文件 URI，如：/100.jpg */
		private static String PATH = "/test";
		
		private static token tk = null;
		
		public static void main(String []args) {
			 tk = new token();
			testToToken();
		}
		
		public static void testToToken() {
			String sign = tk.toToken(KEY, PATH);
			System.out.println(sign);
		}
}
