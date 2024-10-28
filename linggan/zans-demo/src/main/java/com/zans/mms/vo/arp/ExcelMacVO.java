package com.zans.mms.vo.arp;

import com.zans.base.office.annotation.ExcelProperty;
import lombok.Data;


/**
 * @Author pancm
 * @Description mac地址来源界面Excel导出配置
 * @Date  2020/7/30
 * @Param
 * @return
 **/
@Data
public class ExcelMacVO {

    @ExcelProperty(value = "mac地址", index = 1, width = 15)
    private String macAddr;

    @ExcelProperty(value = "厂商", index = 2, width = 30)
    private String company;





}
