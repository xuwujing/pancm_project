package com.zans.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class PagePlusResult<T> extends PageResult<T>{

    @JSONField(name = "allList")
    private List<T> allList;

    public PagePlusResult(long totalNum, List<T> list, int pageSize, int pageNum, List<T> allList) {
        super(totalNum, list, pageSize, pageNum);
        this.allList = allList;
    }
}
