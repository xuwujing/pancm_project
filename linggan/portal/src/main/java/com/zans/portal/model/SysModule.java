package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;

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

    /**
     * 获取菜单id
     *
     * @return id - 菜单id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置菜单id
     *
     * @param id 菜单id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取上级id
     *
     * @return parent_id - 上级id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置上级id
     *
     * @param parentId 上级id
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取菜单名
     *
     * @return name - 菜单名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置菜单名
     *
     * @param name 菜单名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取图标名，icon等
     *
     * @return icon - 图标名，icon等
     */
    public String getIcon() {
        return icon;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * 获取路径
     *
     * @return route - 路径
     */
    public String getRoute() {
        return route;
    }

    /**
     * 设置路径
     *
     * @param route 路径
     */
    public void setRoute(String route) {
        this.route = route == null ? null : route.trim();
    }

    /**
     * 获取同级的顺序，小的靠前
     *
     * @return seq - 同级的顺序，小的靠前
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置同级的顺序，小的靠前
     *
     * @param seq 同级的顺序，小的靠前
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "SysModule{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", menuType=" + menuType +
                ", visible=" + visible +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", sort='" + sort + '\'' +
                ", route='" + route + '\'' +
                ", seq=" + seq +
                ", enable=" + enable +
                '}';
    }
}