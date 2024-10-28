package com.zans.portal.vo.common;

import io.swagger.annotations.ApiModelProperty;
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
public class TreeSelectTopo {

    private Object id;

    private String name;
    /**
     * 第二个核心交换ip
     */
    private String name2;

    private Object data;

    private boolean is_outline = true;

    @ApiModelProperty(name = "type", value = "type 2:areaId;1:regionId;0:根节点")
    private Integer type;

    @ApiModelProperty(name = "topoDataType", value = "1-两层结构显示核心+所有接入,access_num<=12;2-显示核心+12台接入交换机,access_num>12;" +
            "3-三层结构显示核心+全部汇聚+ 每台汇聚下3台接入,aggregation_num<=3;" +
            "4-三层结构显示核心+全部汇聚+ 每台汇聚下3台接入,4<=aggregation_num<=8;" +
            "5-三层结构显示核心+8台汇聚+每台汇聚下3台接入,aggregation_num>8;")
    private Integer treeTopoDataType;

    /**
     * 排序
     */
    private Integer seq;

    private List<TreeSelectTopo> children;

    public void addChild(TreeSelectTopo child) {
        if (children == null) {
            children = new LinkedList<>();
        }
        children.add(child);
    }
}
