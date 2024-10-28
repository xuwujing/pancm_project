package com.zans.portal.vo.msg;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.SYS_MSG_RECEIVER_ALL;
import static com.zans.portal.config.GlobalConstants.SYS_MSG_RECEIVER_ALL_NAME;

/**
 * @author xv
 * @since 2020/3/8 12:33
 */
@ApiModel
@Data
public class MsgRespVO {

    private Integer id;

    @ApiModelProperty(name = "title", value = "标题")
    private String title;

    @ApiModelProperty(name = "content", value = "内容")
    private String content;

    @ApiModelProperty(name = "module", value = "模块")
    private Integer module;

    @ApiModelProperty(name = "module_name", value = "模块名称")
    @JSONField(name = "module_name")
    private String moduleName;

    @ApiModelProperty(name = "link_key", value = "消息外链主键")
    @JSONField(name = "link_key")
    private String linkKey;

    @JSONField(name = "create_time", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "create_time", value = "消息创建时间")
    private Date createTime;

    @ApiModelProperty(name = "receiver", value = "消息接收人")
    private Integer receiver;

    @ApiModelProperty(name = "receiver_name", value = "消息接收人姓名")
    @JSONField(name = "receiver_name")
    private String receiverName;

    @ApiModelProperty(name = "readed", value = "阅读状态")
    private Integer readed;

    @ApiModelProperty(name = "readed_name", value = "阅读状态名称")
    @JSONField(name = "readed_name")
    private String readedName;

    @JSONField(name = "read_time", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "read_time", value = "阅读时间")
    private Date readTime;

    public void resetReadedNameByMap(Map<Object, String> map) {
        Integer status = this.readed;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setReadedName(name);
    }

    public void resetReceiverName() {
        if (this.receiver != null && this.receiver == SYS_MSG_RECEIVER_ALL) {
            this.setReceiverName(SYS_MSG_RECEIVER_ALL_NAME);
        }
    }
}
