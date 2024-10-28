package com.zans.base.util;

/**
 * @author qinkuan
 * 密码校验
 *
 */
public class CheckPassHelper {
    /**
     * 不能包含有连续四位及以上顺序(或逆序)数字
     * @param password
     * @return
     */
    public static boolean checkNumSeq (String password) {
        if (password != null) {
            int len = password.length();
            for (int i = 0; i < len; ++i) {
                if (i + 3 < len) {
                    char c1 = password.charAt(i) ;
                    int c2 = password.charAt(i + 1);
                    int c3 = password.charAt(i + 2);
                    int c4 = password.charAt(i + 3);
                    if(!Character.isDigit(c1) ||!Character.isDigit(c2)||!Character.isDigit(c3)||!Character.isDigit(c4)) {
                        continue;
                    }
                    int m = c2 - c1;
                    if(m == 1 || m == -1){
                        if((c3 - c2) == m && (c4 - c3) == m) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 不能包含有连续四位及以上顺序(或逆序)字母，不区分大小写
     * @param password
     * @return
     */
    public static boolean checkSeqChar(String password){
        if (password != null) {
            String pwd = password.toUpperCase();
            int len = pwd.length();
            for (int i = 0; i < len; ++i) {
                if (i + 3 < len) {
                    char u1 = pwd.charAt(i);
                    char u2 = pwd.charAt(i + 1);
                    char u3 = pwd.charAt(i + 2);
                    char u4 = pwd.charAt(i + 3);
                    int m = u2 - u1;
                    if (m == 1 || m == -1) {
                        if (u3 - u2 == m && u4 - u3 == m) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 不能包含有连续四位及以上重复字符，字母不区分大小写
     * @param password
     * @return
     */
    public static boolean isRepeat4Times(String password){
        if (password != null) {
            String pwd = password.toUpperCase();
            int len = pwd.length();
            for (int i = 0; i < len; ++i) {
                if (i + 3 < len) {
                    int u1 = pwd.charAt(i);
                    int u2 = pwd.charAt(i + 1);
                    int u3 = pwd.charAt(i + 2);
                    int u4 = pwd.charAt(i + 3);
                    if (u1 == u2 && u2 == u3 && u3 == u4) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
