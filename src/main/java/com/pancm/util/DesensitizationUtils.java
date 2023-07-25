package com.pancm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author pancm
 * @Description 脱敏工具类
 * @Date  2023/7/21
 * @Param
 * @return
 **/
public class DesensitizationUtils {

    public static String desensitize(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }

        // 脱敏手机号
        data = desensitizePhoneNumber(data);

        // 脱敏邮箱
        data = desensitizeEmail(data);

        // 脱敏身份证号
        data = desensitizeIdNumber(data);

        // 脱敏银行卡号
        data = desensitizeBankCardNumber(data);



        // 脱敏省份
        data = desensitizeProvince(data);

        // 脱敏城市
        data = desensitizeCity(data);

        // 脱敏区县
        data = desensitizeDistrict(data);


        return data;
    }

    private static String desensitizeProvince(String address) {
        // 省份正则表达式
        String regex = "(.*省|.*自治区|.*特别行政区)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        if (matcher.find()) {
            String province = matcher.group();
            return address.replace(province, "*****");
        }
        return address;
    }

    private static String desensitizeCity(String address) {
        // 城市正则表达式
        String regex = "(.*市)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        if (matcher.find()) {
            String city = matcher.group();
            return address.replace(city, "*****");
        }
        return address;
    }

    private static String desensitizeDistrict(String address) {
        // 区县正则表达式
        String regex = "(.*区|.*县)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        if (matcher.find()) {
            String district = matcher.group();
            return address.replace(district, "*****");
        }
        return address;
    }



    private static String desensitizePhoneNumber(String data) {
        // 手机号正则表达式
        String regex = "(1\\d{2})\\d{4}(\\d{4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        return matcher.replaceAll("$1****$2");
    }

    private static String desensitizeEmail(String data) {
        // 邮箱正则表达式
        String regex = "(\\w+)@(\\w+\\.\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        return matcher.replaceAll("$1****@$2");
    }

    private static String desensitizeIdNumber(String data) {
        // 身份证号正则表达式
        String regex = "(\\d{6})\\d{8}(\\w{4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        return matcher.replaceAll("$1********$2");
    }

    private static String desensitizeBankCardNumber(String data) {
        // 银行卡号正则表达式
        String regex = "(\\d{4})\\d{8}(\\d{4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        return matcher.replaceAll("$1********$2");
    }



    public static void main(String[] args) {
        String phoneNumber = "13812345678";
        String email = "johndoe@example.com";
        String idNumber = "110101199001011234";
        String bankCardNumber = "622202********1234";

        System.out.println(desensitize(phoneNumber)); // Output: 138****5678
        System.out.println(desensitize(email)); // Output: joh****@example.com
        System.out.println(desensitize(idNumber)); // Output: 110101********1234
        System.out.println(desensitize(bankCardNumber)); // Output: 622202********1234

        String address = "上海市上海市浦东新区哈哈哈街道123弄234好";
        System.out.println(desensitize(address)); // Output: *****区*****街道*****
    }
}
