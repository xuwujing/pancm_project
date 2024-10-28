package com.zans.portal.vo.vul;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Data
public class VulHostVul implements Serializable {
    private Long id;

    private String hostId;

    private String jobId;

    private String ipAddr;

    private String vulType;

    private String vulLevel;

    private String vulName;

    private String cve;

    private String cnnvd;

    private String cnvd;

    private Date createTime;

    private String source;

    private String port;

    private String vulObject;

    private String vulMessage;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}