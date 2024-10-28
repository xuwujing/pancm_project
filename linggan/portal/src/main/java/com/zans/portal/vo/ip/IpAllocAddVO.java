package com.zans.portal.vo.ip;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xv
 * @since 2020/3/24 13:50
 */
@ApiModel
@Data
public class IpAllocAddVO {

    @NotEmpty(message = "项目名称必填")
    @JSONField(name = "project_name")
    private String projectName;

    @NotNull(message = "所属区域必填")
    @JSONField(name = "area_id")
    private Integer areaId;

    @NotNull(message = "设备类型必填")
    @JSONField(name = "device_type")
    private Integer deviceType;

    @NotEmpty(message = "承建单位必填")
    private String contractor;

    @NotEmpty(message = "联系人必填")
    @JSONField(name = "contractor_person")
    private String contractorPerson;

    @JSONField(name = "contractor_phone")
    private String contractorPhone;

    @JSONField(name = "file_name")
    private String fileName;

    @JSONField(name = "file_path")
    private String filePath;
}
