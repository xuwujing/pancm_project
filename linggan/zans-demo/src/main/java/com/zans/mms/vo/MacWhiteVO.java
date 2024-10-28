package com.zans.mms.vo;

import com.zans.base.vo.BasePage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "mac_white")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MacWhiteVO extends BasePage implements Serializable{
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 终端mac地址，17位
     */
    private String username;

    /**
     * 备注信息
     */
    private String remark;
    
    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

}