package com.pancm.test.ioTest.doc;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeParser {

    public static void main(String[] args) {
        String resume = "张三，男，25岁，本科学历，Java软件工程师，具有3工作经验。联系方式：手机：13888888888，邮箱：zhangsan@xxx.com。";
        String name = extractName(resume);
        String gender = extractGender(resume);
        int age = extractAge(resume);
        String education = extractEducation(resume);
        String profession = extractProfession(resume);
        int workYears = extractWorkYears(resume);
//        String mobile = extractMobile(resume);
//        String email = extractEmail(resume);

        System.out.println("姓名：" + name);
        System.out.println("性别：" + gender);
        System.out.println("年龄：" + age);
        System.out.println("学历：" + education);
        System.out.println("职业：" + profession);
        System.out.println("工作年限：" + workYears);
//        System.out.println("手机号码：" + mobile);
//        System.out.println("电子邮箱：" + email);
    }

    public static String extractName(String resume) {
        List<Term> terms = HanLP.segment(resume);
        for (int i = 0; i < terms.size(); i++) {
            Term term = terms.get(i);
            if (term.nature.toString().equals("nr")) {
                return term.word;
            }
        }
        return null;
    }

    public static String extractGender(String resume) {
        Pattern pattern = Pattern.compile("男|女");
        Matcher matcher = pattern.matcher(resume);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public static int extractAge(String resume) {
        Pattern pattern = Pattern.compile("\\d+岁");
        Matcher matcher = pattern.matcher(resume);
        if (matcher.find()) {
            String ageStr = matcher.group().substring(0, matcher.group().length() - 1);
            return Integer.parseInt(ageStr);
        } else {
            return -1;
        }
    }

    public static String extractEducation(String resume) {
        List<Term> terms = HanLP.segment(resume);
        for (int i = 0; i < terms.size(); i++) {
            Term term = terms.get(i);
            if (term.word.equals("学历") && i < terms.size() - 1) {
                return terms.get(i + 1).word;
            }
        }
        return null;
    }

    public static String extractProfession(String resume) {
        List<String> keywords = HanLP.extractKeyword(resume, 5);
        if (keywords.size() > 0) {
            return keywords.get(0);
        } else {
            return null;
        }
    }

    public static int extractWorkYears(String resume) {
        Pattern pattern = Pattern.compile("\\d+年工作经验");
        Matcher matcher = pattern.matcher(resume);
        if (matcher.find()) {
            return 1;
        }
        return -1;
    }
}