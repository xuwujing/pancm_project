package com.zans.mms.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Table(name = "sys_module")
public class SysModule implements Serializable {
    /**
     * 菜单id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 上级id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 菜单类型，1，目录；2，菜单
     */
    @Column(name = "menu_type")
    private Integer menuType;

        /**
     * 1,pc;2,小程序
     */
    @Column(name = "module_type")
    private Integer moduleType;


    /**
     * 显示状态，1，显示；2，隐藏
     */
    @Column(name = "visible")
    private Integer visible;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 图标名，icon等
     */
    private String icon;

    /**
     * 菜单序号，小的优先
     */
    private String sort;

    /**
     * 路径
     */
    private String route;

    /**
     * 同级的顺序，小的靠前
     */
    private Integer seq;

    private Integer enable;

    private static final long serialVersionUID = 1L;


}