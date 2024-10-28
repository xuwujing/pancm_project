package com.zans.portal.vo.common;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * TreeSelect 数据容器，和 https://www.vue-treeselect.cn/ 配套
 *
 * @author xv
 * @since 2020/3/7 13:21
 */
@Builder
@Data
public class TreeSelect {

    private Object id;

    private String label;

    private Object data;

    /**
     * 排序
     */
    private Integer seq;

    private Integer level;

    private List<TreeSelect> children;

    private Boolean isShow;

    public void addChild(TreeSelect child) {
        if (children == null) {
            children = new LinkedList<>();
        }
        children.add(child);
    }
}
