package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户文件详细信息(FortUserFileEx)实体类
 *
 * @author beixing
 * @since 2021-08-19 10:48:49
 */
@Data
public class FortUserFileEx implements Serializable {
    private static final long serialVersionUID = -98170399773199751L;
    private Long id;
    /**
     * 文件uuid
     */
    private String fileUuid;
    /**
     * 文件服务器的ip
     */
    private String ip;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户的文件路径(linux)
     */
    private String url;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 文件状态
     */
    private Integer fileStatus;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
