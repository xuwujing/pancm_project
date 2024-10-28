package com.zans.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sys_constant")
public class SysConstant {

    @Id
    private Integer id;

    @Column(name = "constant_key")
    private String constantKey;

    @Column(name = "constant_value")
    private String constantValue;

    private Integer status;

    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    public SysConstant(String constantKey) {
        this.constantKey = constantKey;
    }
}
