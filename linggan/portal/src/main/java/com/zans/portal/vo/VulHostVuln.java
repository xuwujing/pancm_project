package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 漏洞详情表(VulHostVuln)实体类
 *
 * @author beixing
 * @since 2021-11-25 16:58:25
 */
@Data
public class VulHostVuln implements Serializable {
    private static final long serialVersionUID = -69997325611070009L;
    /**
     * id
     */
    private Long id;
    /**
     * vul_host.id
     */
    private Long vulHostId;
    private String port;
    /**
     * Webvlun;sysvlun，系统漏洞;password，弱口令;compliance，边界完整性，from agent;other，其它类型
     */
    private String vulnType;
    /**
     * 1，info，提示，蓝色;2，low，低危，绿色;3，medium，中危，黄色;4，high，高危，橙色;5，critical，极危，红色
     */
    private Integer vulnLevel;
    /**
     * 漏洞名称
     */
    private String vulnName;
    /**
     * 对象名称，如网页地址，redis地址等
     */
    private String vulnObject;
    /**
     * 漏洞描述
     */
    private String vulnMessage;
    /**
     * 解决方案建议
     */
    private String vulSolution;
    /**
     * CVE-2020-XXXX
     */
    private String cve;
    /**
     * CNNVD-202001-XXXX
     */
    private String cnnvd;
    /**
     * CNVD-2020-XXXXX
     */
    private String cnvd;
    /**
     * 创建时间
     */
    private String createTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
