package com.zans.mms.vo.perm;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.mms.model.SysModule;
import lombok.Data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.zans.base.config.GlobalConstants.SYS_MODULE_MENU_TYPE_OPERATION;

/**
 * @author xv
 * @since 2020/3/6 23:32
 */
@Data
public class PermissionRespVO {

    @JSONField(name = "perm_id")
    private Integer permId;

    @JSONField(name = "data_perm_status")
    private Integer dataPermStatus;

    @JSONField(name = "perm_name")
    private String permName;

    @JSONField(name = "perm_desc")
    private String permDesc;

    private Integer module;

    @JSONField(name = "module_name")
    private String moduleName;

    private String route;

    /**
     * 1, 目录
     * 2，菜单按钮
     * 3. 权限 - 操作按钮
     */
    private Integer type;

    @JSONField(name = "route_list")
    private List<String> routeList;

    private String api;

    private Integer seq;

    @JSONField(name = "api_list")
    private List<String> apiList;


    private Integer enable;

    @JSONField(name = "create_time", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(name = "update_time", format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 设置属性 route、api、type
     */
    public void resetProperties() {
        routeList = new LinkedList<>();
        if (route != null) {
            String[] array = route.split(",");
            for (String s : array) {
                routeList.add(s.trim());
            }
        }

        apiList = new LinkedList<>();
        if (api != null) {
            String[] array = api.split(",");
            for (String s : array) {
                apiList.add(s.trim());
            }
        }
        this.type = SYS_MODULE_MENU_TYPE_OPERATION;
    }

    public void fromModule(SysModule module) {
        if (module == null) {
            return;
        }
        this.permId = module.getId();
        this.permName = module.getName();
        this.route = module.getRoute();
        this.seq = module.getSeq();
        this.module = module.getParentId();
        this.type = module.getMenuType();
    }
}
