package com.zans.vo.node;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author xv
 * @since 2020/5/7 18:19
 */
@Data
public class NodeRegVO {


    private String nodeId;

    private String nodeType;

    private String ip;

    private Integer port;

    private String name;

    private String version;

    private String profile;

    @JSONField(name = "build_version")
    private String buildVersion;
}
