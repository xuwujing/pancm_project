package com.zans.portal.model;

import com.zans.base.vo.BasePage;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 设备类型用户标签表(TDeviceTag)实体类
 *
 * @author beixing
 * @since 2022-02-23 16:00:03
 */
@Data
@Table(name = "t_device_tag")
public class TDeviceTag implements Serializable {
    private static final long serialVersionUID = 255271838118279844L;
    @Column(name = "id")
    private Integer id;
    /**
     * 名称
     */
    @Column(name = "tag_name")
    private String tagName;
    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;
    /**
     * 启用状态
     */
    @Column(name = "enable_status")
    private Integer enableStatus;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
