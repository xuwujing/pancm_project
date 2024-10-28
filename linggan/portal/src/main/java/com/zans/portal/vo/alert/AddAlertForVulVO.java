package com.zans.portal.vo.alert;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * create by: beiming
 * create time: 2021/12/9 15:55
 */
@Data
public class AddAlertForVulVO {
    @NotBlank
    private String mac;

    @NotBlank
    private String ipAddr;

    @NotBlank
    private String scanTime;

    @NotBlank
    private String descJson;

    private String alertLevel;
}
