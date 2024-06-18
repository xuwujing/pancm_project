package com.pancm.test.characterEncode;

/**
 * The type Test.
 *
 * @author pancm
 * @Title: Test
 * @Description:编码测试
 * @Version:1.0.0
 * @date 2018年5月31日
 */
public class Test {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Test t=new Test();
		t.testCode();
	}

	private void testCode(){
		System.out.println("中文");
		System.out.println("中文".getBytes());
		try {

			System.out.println("中文".getBytes("GB2312"));

			System.out.println("中文".getBytes("ISO8859_1"));

			System.out.println(new String("中文".getBytes()));
			System.out.println(new String("中文".getBytes(), "GB2312"));
			System.out.println(new String("中文".getBytes(), "ISO8859_1"));

			System.out.println(new String("中文".getBytes("GB2312")));

			System.out.println(new String("中文".getBytes("GB2312"), "GB2312"));

			System.out.println(new String("中文".getBytes("GB2312"), "ISO8859_1"));

			System.out.println(new String("中文".getBytes("ISO8859_1")));

			System.out.println(new String("中文".getBytes("ISO8859_1"), "GB2312"));

			System.out.println(new String("中文".getBytes("ISO8859_1"), "ISO8859_1"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("==:"+getcode("123"));
	}


    /**
     * Gets .
     *
     * @param str the str
     * @return the
     */
    public String getcode(String str) {
		String[] encodelist = {"UTF-8","ISO-8859-1",  "GB2312",  "GBK", "gb 18030", "Big5", "UTF-16LE", "Shift_JIS",
				"EUC-JP", "ISO-2002-JP" };
		for (int i = 0; i < encodelist.length; i++) {
			try {
				if (str.equals(new String(str.getBytes(encodelist[i]), encodelist[i]))) {
					System.out.println("当前编码:" + encodelist[i]);
					return encodelist[i];
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		return "";
	}
}
