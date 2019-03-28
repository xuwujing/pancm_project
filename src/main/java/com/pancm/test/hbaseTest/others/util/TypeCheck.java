package com.pancm.test.hbaseTest.others.util;

import java.util.List;

/**
 * Created by zhenjie.wang on 2015/8/13.
 */
public class TypeCheck {

	private enum LongtypeCheck {
        /**
         * Accessid longtype check.
         */
// sessionid,sqlid,dip,replyaccessid,smac,dmac;
		accessid,
        /**
         * Replyaccessid longtype check.
         */
        replyaccessid,
        /**
         * C 4 longtype check.
         */
        c4;
	}

	private enum InttypeCheck {
        /**
         * Effectrow inttype check.
         */
// policyid,effectrow,shgid,apptypeid,srcid,datafrom,policyalertgrade;
		effectrow,
        /**
         * Shgid inttype check.
         */
        shgid,
        /**
         * Apptypeid inttype check.
         */
        apptypeid,
        /**
         * Srcid inttype check.
         */
        srcid,
        /**
         * Datafrom inttype check.
         */
        datafrom,
        /**
         * Policyalertgrade inttype check.
         */
        policyalertgrade;// ,poid;
	}

	private enum FloattypeCheck {
        /**
         * Code floattype check.
         */
        code,
        /**
         * Level floattype check.
         */
        level,
        /**
         * Cost floattype check.
         */
        cost,
        /**
         * Resultflag floattype check.
         */
        resultflag;
	}

    /**
     * Check long boolean.
     *
     * @param source the source
     * @return the boolean
     */
    public static boolean checkLong(String source) {
		for (LongtypeCheck l : LongtypeCheck.values()) {
			if (l.toString().equals(source.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

    /**
     * Check int boolean.
     *
     * @param source the source
     * @return the boolean
     */
    public static boolean checkInt(String source) {
		for (InttypeCheck i : InttypeCheck.values()) {
			if (i.toString().equals(source.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

    /**
     * Check float boolean.
     *
     * @param source the source
     * @return the boolean
     */
    public static boolean checkFloat(String source) {
		for (FloattypeCheck f : FloattypeCheck.values()) {
			if (f.toString().equals(source.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

    /**
     * Check string boolean.
     *
     * @param source  the source
     * @param strList the str list
     * @return the boolean
     */
    public static boolean checkString(String source, List<String> strList) {
		if (strList.contains(source)) {
			return true;
		}
		return false;
	}
}
