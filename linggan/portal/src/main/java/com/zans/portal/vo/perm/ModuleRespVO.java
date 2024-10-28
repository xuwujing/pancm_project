package com.zans.portal.vo.perm;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.model.SysModule;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.zans.portal.config.GlobalConstants.SYS_MODULE_MENU_TYPE_OPERATION;

/**
 * @author yhj
 */
@Data
@ApiModel
public class ModuleRespVO {

    @JSONField(name = "id")
    private Integer id;

    @JSONField(name = "parent_id")
    @NotNull
    private Integer parentId;

    @JSONField(name = "menu_type")
    @NotNull
    private Integer menuType;

    @JSONField(name = "visible")
    private Integer visible;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "icon")
    private String icon;

    @JSONField(name = "index")
    private String sort;

    @JSONField(name = "seq")
    private Integer seq;

    @JSONField(name = "enable")
    private Integer enable;

    @JSONField(name = "route")
    private String route;

    public void fromModule(SysModule module) {
        if (module == null) {
            return;
        }
        this.id = module.getId();
        this.parentId = module.getParentId();
        this.menuType = module.getMenuType();
        this.visible = module.getVisible();
        this.name = module.getName();
        this.icon = module.getIcon();
        this.sort = module.getSort();
        this.seq = module.getSeq();
        this.enable = module.getEnable();
        this.route = module.getRoute();
    }

}
