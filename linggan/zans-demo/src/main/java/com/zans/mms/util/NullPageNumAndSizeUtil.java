package com.zans.mms.util;


import com.zans.base.vo.BasePage;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/25
 */
public class NullPageNumAndSizeUtil {

    private static final Integer pageNum = 1;

    private static final Integer pageSize = 15;

    public static Object getDefaultPageNumAndSize(Object object){
        if (object instanceof BasePage){
            if (((BasePage) object).getPageNum() == null){
                ((BasePage) object).setPageNum(pageNum);
            }
            if (((BasePage) object).getPageSize() == null){
                ((BasePage) object).setPageSize(pageSize);
            }
        }
        return object;
    }


}
