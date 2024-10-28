package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 附件表(BaseVfs)实体类
 *
 * @author beixing
 * @since 2021-03-02 16:22:01
 */
@ApiModel(value = "BaseVfs", description = "附件表")
@Data
@Table(name = "base_vfs")
public class BaseVfs implements Serializable {
    private static final long serialVersionUID = -86849236142587743L;
    @Column(name = "id")
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * 附件编号
     */
    @Column(name = "adjunct_id")
    @ApiModelProperty(value = "附件编号")
    private String adjunctId;
    /**
     * 图片存储流
     */
    @Column(name = "raw_stream")
    @ApiModelProperty(value = "图片存储流")
    private Object rawStream;
    /**
     * 压缩图片存储流
     */
    @Column(name = "thumbnail_stream")
    @ApiModelProperty(value = "压缩图片存储流")
    private Object thumbnailStream;
    /**
     * 原始图片存储路径
     */
    @Column(name = "raw_file_path")
    @ApiModelProperty(value = "原始图片存储路径")
    private String rawFilePath;
    /**
     * 缩略图片存储路径
     */
    @Column(name = "thumbnail_file_path")
    @ApiModelProperty(value = "缩略图片存储路径")
    private String thumbnailFilePath;
    /**
     * 存储类型,0:流 1:路径
     */
    @Column(name = "img_type")
    @ApiModelProperty(value = "存储类型,0:流 1:路径")
    private Integer imgType;
    /**
     * content_type
     */
    @Column(name = "content_type")
    @ApiModelProperty(value = "content_type")
    private String contentType;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
