package com.pancm.test.leetcode;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。

示例 1:

输入: 123
输出: 321
 示例 2:

输入: -123
输出: -321
示例 3:

输入: 120
输出: 21
注意:

假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−231,  231 − 1]。请根据这个假设，如果反转后整数溢出那么就返回 0。

链接：https://leetcode-cn.com/problems/reverse-integer


 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/5/18
 */
public class LeetCode2 {

    public static void main(String[] args) {
        int i = 321;
        int j = 120;
        int o = 101;
        int k = -120;
        System.out.println(reverse(i)); //123
        System.out.println(reverse(j)); // 21
        System.out.println(reverse(o)); // 101
        System.out.println(reverse(k)); // -21
        System.out.println(reverse2(i)); //123
        System.out.println(reverse2(j)); // 21
        System.out.println(reverse2(o)); // 101
        System.out.println(reverse2(k)); // -21


    }

    public static int reverse(int x) {
        String s =reverse(String.valueOf(x));
        if (s.contains("-")){
            s=s.replace("-","");
            return Integer.parseInt("-"+s);
        }
        return Integer.parseInt(s);
    }
    public static String reverse(String str) {
        if ("0".equals(str)||(null == str || str.trim().length() == 0)) {
            return str;
        }
        return reverse(str.substring(1)) + str.charAt(0);
    }

    /**
     * 最优解: https://leetcode-cn.com/problems/reverse-integer/solution/tu-jie-7-zheng-shu-fan-zhuan-by-wang_ni_ma/
     * 利用取模实现
     *
     **/
    public static int reverse2(int x) {
        int res = 0;
        while(x!=0) {
            //每次取末尾数字
            int tmp = x%10;
            //判断是否 大于 最大32位整数
            if (res>214748364 || (res==214748364 && tmp>7)) {
                return 0;
            }
            //判断是否 小于 最小32位整数
            if (res<-214748364 || (res==-214748364 && tmp<-8)) {
                return 0;
            }
            res = res*10 + tmp;
            x /= 10;
        }
        return res;
    }



}

