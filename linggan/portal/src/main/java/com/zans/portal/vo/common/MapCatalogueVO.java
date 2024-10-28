package com.zans.portal.vo.common;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
@Data
public class MapCatalogueVO {
    /**
     * 部门id
     */
    @Id
    private Integer id;

    /**
     * 上级id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 1.目录；2.菜单
     */
    @Column(name = "department_type")
    private Integer departmentType;

    /**
     * 1,显示；2，隐藏
     */
    private Integer visible;

    /**
     * 图标名，icon等
     */
    private String icon;

    /**
     * 同级的顺序，小的靠前
     */
    private Integer seq;

    private Integer enable;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private Integer createId;

    /**
     * 修改人
     */
    @Column(name = "update_id")
    private Integer updateId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Transient
    private String deviceStatus;

    @JSONField(name = "brand_name")
    @Transient
    private String brandName;

    @JSONField(name = "device_type")
    @Transient
    private String deviceType;

    @JSONField(name = "type_name")
    @Transient
    private String typeName;

}
