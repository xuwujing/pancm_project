package com.zans.mms.vo.common;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Builder
@Data
public class TreeSelect {

    private Object id;

    private String label;

    private Object data;

    /**
     * 数据权限flag
     */
    private Object dataPermFlag;

    /**
     * 数据权限描述
     */
    private Object dataPermDesc;

    /**
     * 排序
     */
    private Integer seq;

    private List<TreeSelect> children;

    public void addChild(TreeSelect child) {
        if (children == null) {
            children = new LinkedList<>();
        }
        children.add(child);
    }
}
