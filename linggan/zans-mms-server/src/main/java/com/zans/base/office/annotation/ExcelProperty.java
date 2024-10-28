package com.zans.base.office.annotation;

import java.lang.annotation.*;

/**
 * @author xv
 * @since 2020/3/21 11:52
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {

    String name() default "";

    String[] value() default {""};

    int index() default -1;

    boolean ignore() default false;

    boolean isSelect() default false;

    String[] colors() default {""};

    /**
     * 数据类型，默认String，其它包括 Integer, Date
     * @return
     */
    String type() default "String";

    /**
     * 输出文件，列宽度
     * @return
     */
    int width() default -1;

    /**
     * 校验规则
     * @return
     */
    String[] validate() default {};

    /**
     * "yyyy-MM-dd"
     * @return
     */
    String dateFormat() default "";

    /**
     * "#.##%"
     * @return
     */
    String numFormat() default "";

}
