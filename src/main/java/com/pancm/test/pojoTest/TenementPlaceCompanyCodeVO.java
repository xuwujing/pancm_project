package com.pancm.test.pojoTest;

import lombok.Data;

import java.io.Serializable;

@Data
public class TenementPlaceCompanyCodeVO implements Serializable {


    /**
     * 归属门店编码
     */
    private String shopCode;
    /**
     * 公司编号
     */
    private String companyCode;
    /**
     * 公共事业费归属公司编号
     */
    private String commonCompanyCode;



}