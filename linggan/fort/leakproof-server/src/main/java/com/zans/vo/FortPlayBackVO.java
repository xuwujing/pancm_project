package com.zans.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fort_play_back")
public class FortPlayBackVO extends BasePage implements Serializable {

    /**
     * 自增ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user")
    private String user;

    /**
     * 服务ip
     */
    @Column(name = "server_ip")
    private String serverIp;

    /**
     * 操作人uo
     */
    @Column(name = "source_ip")
    private String sourceIp;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 回放相对路径
     */
    @Column(name = "play_back_url_guac")
    private String playBackUrlGuac;

    /**
     * 解码后文件相对路径
     */
    @Column(name = "play_back_url_m4v")
    private String playBackUrlM4v;

    /**
     * 状态 0待解码 1解码中 2解码完成
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 文件大小
     */
    private String fileSize;

    private String videoTime;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    private String startTime;

    private String endTime;

    private String playBackUrl;

}
