package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "base_maintaion_facility")
public class BaseMaintaionFacility implements Serializable {


    /**
     * 设备ID
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 所属公司,关联base_maintain的id
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 设备编号,excel中的设备ID
     */
    @Column(name = "device_code")
    private String deviceCode;

    /**
     * 设备价格D
     */
    @Column(name = "device_price")
    private BigDecimal devicePrice;

    /**
     * 设备分类,一、土建类 二、杆件类  三、线缆类  四、光缆类 五、设备类 六、配件类 七、抢修类
     */
    @Column(name = "device_category")
    private String deviceCategory;

    @Column(name = "device_name")
    private String deviceName;
    @Column(name = "device_spec")
    private String deviceSpec;
    @Column(name = "device_brand")
    private String deviceBrand;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序级别
     */
    private Integer sort;

    /**
     * 创建用户
     */
    private String creator;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private Integer isOccur;

    private static final long serialVersionUID = 1L;


}
