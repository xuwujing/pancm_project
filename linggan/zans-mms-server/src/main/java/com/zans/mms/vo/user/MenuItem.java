package com.zans.mms.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.mms.model.SysModule;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 菜单数据
 * @author xv
 * @since 2020/3/6 16:19
 */
@Data
public class MenuItem {

//    @JSONField(serialize = false)
    private Integer id;

    private String index;

    private String path;

    private String icon;

    private String name;

    private Integer parentId;

    @JSONField(serialize = false)
    private Integer menuType;

    private List<MenuItem> subItem;

    public void addSubItem(MenuItem menuItem) {
        if (subItem == null) {
            subItem = new LinkedList<>();
        }
        subItem.add(menuItem);
    }

    public void fromSysModule(SysModule module) {
        this.id = module.getId();
        this.name = module.getName();
        this.index = module.getSort();
        this.icon = module.getIcon();
        this.path = module.getRoute();
        this.menuType = module.getMenuType();
        this.parentId = module.getParentId();
    }
}
