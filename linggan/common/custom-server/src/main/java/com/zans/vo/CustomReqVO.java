package com.zans.vo;

import com.zans.strategy.QueryReqVO;
import lombok.Data;

import java.util.Map;

/**
 * @author beixing
 * @Title: custom-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/5/17
 */
@Data
public class CustomReqVO {

    /**
     * 页码
     * 必须定义为 protected or public，否则子类不可见
     */
    protected Integer pageNum;

    /**
     * 页大小
     */
    protected Integer pageSize;

    /**
     * 排序字段名称
     */
    protected String sortName;

    /**
     * 排序方式
     */
    protected String sortOrder;

    protected String module;


    private Map<String, QueryReqVO> map;

}
