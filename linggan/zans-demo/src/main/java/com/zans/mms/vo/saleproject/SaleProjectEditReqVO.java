package com.zans.mms.vo.saleproject;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
public class SaleProjectEditReqVO implements Serializable {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 销售项目编号
     */
    private String projectId;

    /**
     * 销售项目名称
     */
    private String projectName;

    /**
     * 创建用户
     */
    private String creator;


    private static final long serialVersionUID = 1L;

}