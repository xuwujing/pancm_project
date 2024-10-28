package com.zans.portal.vo.ip;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import lombok.Data;

/**
 * @author xv
 * @since 2020/3/24 13:55
 */
@Data
public class IpAllocSearchVO extends BasePage {

    @JSONField(name = "device_type")
    private Integer deviceType;

    private Integer area;

    private String contractor;

    @JSONField(name = "project_name")
    private String projectName;

    @JSONField(name = "insert_status")
    private Integer insertStatus;

    @JSONField(name = "contractor_person")
    private String contractorPerson;
}
