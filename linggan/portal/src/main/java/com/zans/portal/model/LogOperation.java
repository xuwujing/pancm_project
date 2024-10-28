package com.zans.portal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "log_operation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogOperation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "op_platform")
    private String opPlatform;

    private String module;

    private String operation;

    @Column(name = "class_name")
    private String className;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "req_data")
    private String reqData;

    @Column(name = "rep_data")
    private String repData;

    @Column(name = "from_ip")
    private String fromIp;

    private String result;

    private String remark;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    public LogOperation(String traceId, String userName, String module, String operation, String repData, String fromIp, String result) {
        this.traceId = traceId;
        this.userName = userName;
        this.module = module;
        this.operation = operation;
        this.repData = repData;
        this.fromIp = fromIp;
        this.result = result;
    }
}