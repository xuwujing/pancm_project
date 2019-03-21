package com.pancm.test.hbaseTest.others.util;

import java.util.List;

/**
 * The type List util.
 */
public class IListUtil {

    /**
     * Implode string.
     *
     * @param strList the str list
     * @return the string
     */
    public static String implode(List<String> strList) {
		String res = "";
		if (strList == null || strList.size() == 0) {
			return res;
		}
		for (String str : strList) {
			res += str + ",";
		}
		return res.substring(0, res.length() - 1);
	}
}
