package com.zans.mms.vo.common;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * create by: beiming
 * create time: 2021/12/23 11:42
 */
@Data
public class OptionSelect {
    private Integer key;
    private String name;

    private List<OptionSelect> children;


    public void addChild(OptionSelect child) {
        if (children == null) {
            children = new LinkedList<>();
        }
        children.add(child);
    }

}
