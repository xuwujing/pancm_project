package com.zans.utils;

import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zans.constant.BaseConstants.*;
import static java.util.regex.Pattern.compile;

public class StringHelper {

    /**
     * 字符串是否全是数字
     *
     * @param str
     * @return
     */
    public static Boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    /**
     * 是否是空的或为0，为null或空字符串或空列表或为0
     *
     * @param target
     * @return
     */
    public static Boolean isEmpty(Object target) {
        if (target == null) {
            return true;
        } else if (target.getClass().equals(Integer.class)) {
            return ((Integer) target) == 0;
        } else if (target.getClass().equals(String.class)) {
            return ((String) target).isEmpty() || ((String) target).replaceAll("\\s", "").isEmpty();
//        }else if (target.getClass().equals(List.class)){
        } else if (target.getClass().equals(String[].class)) {
            return ((String[]) target).length == 0;
        } else if (target instanceof List) {
            return ((List) target).isEmpty();
        } else if (target instanceof Map) {
            return ((Map) target).isEmpty();
        }
        return false;
    }

    /**
     * 去除两端空白字符后是否是空字符串
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 去除两端空白字符后是否不是空字符串
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isNotBlank(String str) {
        return (str != null && str.trim().length() > 0);
    }

    /**
     * 获得异常的详细信息
     *
     * @param e
     * @return
     */
    public static String getErrorInfoFromException(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.close();
            sw.close();
            String result = sw.toString();
            String[] lines = result.split("\r\n");
            int maxleng = 5;
            if (lines.length <= maxleng) {
                return result;
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < maxleng; i++) {
                    sb.append(lines[i]).append("\r\n");
                }
                return sb.toString();
            }

        } catch (Exception e2) {
            return e.toString();
        }
    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getBusinessId() {
        return DateHelper.formatDate(new Date(), "yyyyMMddHHmmssSSS") + RandomHelper.getRandom(2);
    }

    public static String getReqId() {
        return "req-" + getBusinessId();
    }

//    public static boolean isMacValid(String mac) {
//        return isSpaceMacValid(mac) || isShortMacValid(mac) || isLineMacValid(mac);
//    }




    public static String convertMacToLineStyle(String mac) {
        if (mac != null && mac.length() == VALID_MAC_LENGTH) {
            return mac.replaceAll(" ", "-");
        } else {
            return mac;
        }
    }

    public static String convertMacToMin(String mac) {
        if (mac != null) {
            return mac.toLowerCase().replaceAll(" ", "").replaceAll("-", "");
        } else {
            return mac;
        }

    }

    public static String convertMacToBlankStyle(String mac) {
        if (mac != null) {
            String macMin = convertMacToMin(mac);
            if (macMin.length() == VALID_MIN_MAC_LENGTH) {
                return String.format("%s %s %s %s %s %s",
                        macMin.substring(0, 2),
                        macMin.substring(2, 4),
                        macMin.substring(4, 6),
                        macMin.substring(6, 8),
                        macMin.substring(8, 10),
                        macMin.substring(10, 12));
            } else {
                return mac;
            }
        } else {
            return mac;
        }
    }

    public static Integer getIntValue(Object input) {
        if (input == null) {
            return null;
        }
        try {
            String val = input.toString();
            if (isNumeric(val)) {
                return Integer.parseInt(val.trim());
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String join(Object[] array, String separator) {
        if (separator == null) {
            separator = "";
        }
        if (array != null && array.length != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(array[0]);

            for (int i = 1; i < array.length; ++i) {
                sb.append(separator).append(array[i]);
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static String joinCollection(Collection<Object> array, String separator) {
        if (separator == null) {
            separator = "";
        }
        if (array != null && array.size() != 0) {
            StringBuilder sb = new StringBuilder();

            for (Object data : array) {
                sb.append(separator).append(data);
            }
            String result = sb.toString();
            return result.substring(separator.length());
        } else {
            return "";
        }
    }

    public static boolean isValidIp(String ipInput) {
        if (isBlank(ipInput)) {
            return false;
        }
        Pattern pattern = compile("^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$");
        Matcher matcher = pattern.matcher(ipInput);
        try {
            if (!matcher.matches()) {
                return false;
            } else {
                for (int i = 1; i <= 4; i++) {
                    int octet = Integer.parseInt(matcher.group(i));
                    if (octet > 255) {
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static String ipIncrease(String ip) {
        if (!isValidIp(ip)) {
            return null;
        }

        String[] ipArray = ip.split(IP_SPLIT_SEPARATOR);
        int ipEnd = Integer.parseInt(ipArray[3]);
        if (ipEnd >= IP_ASSIGN_LAST) {
            return null;
        }

        return String.format("%s.%s.%s.%d", ipArray[0], ipArray[1], ipArray[2], ipEnd + 1);
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 转换为下划线
     *
     * @param camelCaseName
     * @return
     */
    public static String underScoreToCamelCase(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 转换为驼峰
     *
     * @param underscoreName
     * @return
     */
    public static String camelCaseToUnderScore(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }


    public static String secondToTime(String durationSeconds) {
        if (StringUtils.isEmpty(durationSeconds) || "0".equals(durationSeconds)) {
            return "0";
        }
        Integer second = Integer.parseInt(durationSeconds);
        long d = second / (60 * 60 * 24);
        long h = (second - (60 * 60 * 24 * d)) / 3600;
        long m = (second - 60 * 60 * 24 * d - 3600 * h) / 60;
        long s = second - 60 * 60 * 24 * d - 3600 * h - 60 * m;
        String result = (d == 0 ? "" : d + "天") + (h == 0 ? "" : h + "时") + (m == 0 ? "" : m + "分") + (s == 0 ? "" : s + "秒");
        return result;
    }

    //byte 转 G、M、KB、B
    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    /**
     * 是否是数字或小数
     *
     * @return boolean
     */
    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String reg = "\\d+(\\.\\d+)?";
        return str.matches(reg);
    }

    /**
    * @Author beixing
    * @Description  equals方法实现
     *  StringUtils copy
    * @Date  2021/3/31
    * @Param
    * @return
    **/
    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 != null && cs2 != null) {
            return cs1 instanceof String && cs2 instanceof String ? cs1.equals(cs2) : regionMatches(cs1, false, 0, cs2, 0, Math.max(cs1.length(), cs2.length()));
        }
        return false;
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        if (str1 != null && str2 != null) {
            if (str1 == str2) {
                return true;
            }
            return str1.length() != str2.length() ? false : regionMatches(str1, true, 0, str2, 0, str1.length());

        }
        return str1 == str2;

    }


    private  static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        } else {
            int index1 = thisStart;
            int index2 = start;
            int var8 = length;

            while (var8-- > 0) {
                char c1 = cs.charAt(index1++);
                char c2 = substring.charAt(index2++);
                if (c1 != c2) {
                    if (!ignoreCase) {
                        return false;
                    }
                    if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println(isNumeric("1.2"));
        System.out.println(isNumber("1.2"));
        System.out.println(getBusinessId());

    }
}
