package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户文件详细信息(FortUserFileEx)实体类
 *
 * @author beixing
 * @since 2021-08-19 10:48:49
 */
@Data
@Table(name = "fort_user_file_ex")
@AllArgsConstructor
@NoArgsConstructor
public class FortUserFileEx implements Serializable {
    private static final long serialVersionUID = 948952287143292691L;
    @Column(name = "id")
    private Long id;
    /**
     * 文件uuid
     */
    @Column(name = "file_uuid")
    private String fileUuid;
    /**
     * 文件服务器的ip
     */
    @Column(name = "ip")
    private String ip;
    /**
     * 用户名称
     */
    @Column(name = "user_name")
    private String userName;
    /**
     * 用户的文件路径(linux)
     */
    @Column(name = "url")
    private String url;
    /**
     * 文件名称
     */
    @Column(name = "file_name")
    private String fileName;
    /**
     * 文件类型
     */
    @Column(name = "file_type")
    private String fileType;
    /**
     * 文件大小
     */
    @Column(name = "file_size")
    private String fileSize;
    /**
     * 文件状态
     */
    @Column(name = "file_status")
    private Integer fileStatus;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
