package com.zans.base.vo;

import lombok.Data;

@Data
public class LicenseVO {

    private String expireday;

    private String macadress;

    private String producttype;

    private String productname;

    private String channel;

    private String licensesign;

    private String publicKey;

    private String privateKey;

    private String menuList;

    public LicenseVO() {
    }

    public LicenseVO(String expireday, String macadress, String producttype, String productname, String privateKey, String publicKey, String menuList, String channel) {
        this.expireday = expireday;
        this.macadress = macadress;
        this.producttype = producttype;
        this.productname = productname;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.menuList = menuList;
        this.channel = channel;
    }
}
