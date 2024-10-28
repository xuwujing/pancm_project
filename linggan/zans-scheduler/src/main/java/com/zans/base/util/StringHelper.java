package com.zans.base.util;

import com.zans.base.config.BaseConstants;
import com.zans.base.valid.MacAddressValidator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zans.base.config.BaseConstants.IP_SPLIT_SEPARATOR;
import static com.zans.base.config.BaseConstants.VALID_MAC_LENGTH;
import static com.zans.base.config.BaseConstants.IP_ASSIGN_LAST;
import static java.util.regex.Pattern.compile;

public class StringHelper {

    /**
     * 字符串是否全是数字
     * @param str
     * @return
     */
    public static Boolean isNumeric(String str){
        return str.matches("\\d+");
    }

    /**
     * 是否是空的或为0，为null或空字符串或空列表或为0
     * @param target
     * @return
     */
    public static Boolean isEmpty(Object target){
        if (target == null){
            return true;
        }else if (target.getClass().equals(Integer.class)){
            return ((Integer) target) == 0;
        }else if (target.getClass().equals(String.class)){
            return ((String) target).isEmpty() || ((String) target).replaceAll("\\s","").isEmpty();
//        }else if (target.getClass().equals(List.class)){
        }else if (target.getClass().equals(String[].class)){
            return ((String[]) target).length == 0;
        }else if (target instanceof List){
            return ((List) target).isEmpty();
        }else if (target instanceof Map){
            return ((Map) target).isEmpty();
        }
        return false;
    }

    /**
     * 去除两端空白字符后是否是空字符串
     *
     * @param str
     *            字符串
     * @return boolean
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 去除两端空白字符后是否不是空字符串
     *
     * @param str
     *            字符串
     * @return boolean
     */
    public static boolean isNotBlank(String str) {
        return (str != null && str.trim().length() > 0);
    }

    /**
     * 获得异常的详细信息
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

    public static boolean isMacValid(String mac) {
        return (mac != null && mac.matches(MacAddressValidator.MAC_PATTERN) );
    }

    public static String convertMacToLineStyle(String mac) {
        if (mac != null && mac.length() == VALID_MAC_LENGTH) {
            return mac.replaceAll(" ", "-");
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
        if (array != null && array.length != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(array[0]);

            for(int i = 1; i < array.length; ++i) {
                sb.append(separator).append(array[i]);
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String joinList(List<Object> array) {
        return joinList(array, BaseConstants.SEPARATOR_COMMA);
    }

    public static String joinList(List<Object> array, String separator) {

        if (array != null && array.size() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(array.get(0));

            for(int i = 1; i < array.size(); ++i) {
                sb.append(separator).append(array.get(i));
            }

            return sb.toString();
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


}
