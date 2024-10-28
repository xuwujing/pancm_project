package com.zans.portal.vo.ip;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author xv
 * @since 2020/3/24 13:50
 */
@Data
public class IpAllocRespVO {

    private Integer id;

    @JSONField(name = "project_name")
    private String projectName;

    @JSONField(name = "area")
    private Integer areaId;

    @JSONField(name = "area_name")
    private String areaName;

    @JSONField(name = "device_type")
    private Integer deviceType;

    @JSONField(name = "device_type_name")
    private String deviceTypeName;

    private String contractor;

    @JSONField(name = "contractor_person")
    private String contractorPerson;

    @JSONField(name = "contractor_phone")
    private String contractorPhone;

    @JSONField(name = "file_name")
    private String fileName;

    @JSONField(name = "file_path")
    private String filePath;

    @JSONField(name = "valid_status")
    private Integer validStatus;

    @JSONField(name = "valid_status_name")
    private String validStatusName;


    @JSONField(name = "insert_status")
    private Integer insertStatus;

    @JSONField(name = "insert_status_name")
    private String insertStatusName;

    @JSONField(name = "alloc_day", format = "yyyy-MM-dd")
    private Date allocDay;

    @JSONField(name = "create_time")
    private String createTime;

    @JSONField(name = "update_time")
    private Date updateTime;

    private String template;

    public void setInsertStatusNameByMap(Map<Object, String> map) {
        Integer status = this.insertStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setInsertStatusName(name);
    }

    public void setValidStatusNameByMap(Map<Object, String> map) {
        Integer status = this.validStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setValidStatusName(name);
    }
}
