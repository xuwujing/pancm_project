package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 漏洞详情表(VulHostVuln)实体类
 *
 * @author beixing
 * @since 2021-11-25 16:58:26
 */
@Data
@Table(name = "vul_host_vuln")
public class VulHostVuln extends BasePage implements Serializable {
    private static final long serialVersionUID = 793680722472804359L;
    /**
     * id
     */
    @Column(name = "id")
    private Long id;
    /**
     * vul_host.id
     */
    @Column(name = "ip_addr")
    private String ipAddr;
    @Column(name = "port")
    private String port;
    /**
     * Webvlun;sysvlun，系统漏洞;password，弱口令;compliance，边界完整性，from agent;other，其它类型
     */
    @Column(name = "vul_type")
    private String vulType;
    /**
     * 1，info，提示，蓝色;2，low，低危，绿色;3，medium，中危，黄色;4，high，高危，橙色;5，critical，极危，红色
     */
    @Column(name = "vul_level")
    private String vulLevel;
    /**
     * 漏洞名称
     */
    @Column(name = "vul_name")
    private String vulName;
    /**
     * 对象名称，如网页地址，redis地址等
     */
    @Column(name = "vul_object")
    private String vulObject;
    /**
     * 漏洞描述
     */
    @Column(name = "vul_message")
    private String vulMessage;
    /**
     * 解决方案建议
     */
    @Column(name = "vul_solution")
    private String vulSolution;
    /**
     * CVE-2020-XXXX
     */
    @Column(name = "cve")
    private String cve;
    /**
     * CNNVD-202001-XXXX
     */
    @Column(name = "cnnvd")
    private String cnnvd;
    /**
     * CNVD-2020-XXXXX
     */
    @Column(name = "cnvd")
    private String cnvd;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
