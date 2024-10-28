package com.zans.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.util.StringHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class BasePage {

    /**
     * 页码
     * 必须定义为 protected or public，否则子类不可见
     */
    @ApiModelProperty(name = "page_num",value = "分页 - 第几页")
//    @JSONField(name = "page_num")
    protected Integer pageNum;

    /**
     * 页大小
     */
    @ApiModelProperty(name = "page_num",value = "分页 - 每页记录数")
//    @JSONField(name = "page_size")
    protected Integer pageSize;

    /**
     * 排序字段名称
     */
    @ApiModelProperty(name = "sort_name",value = "分页 - 排序列名")
//    @JSONField(name = "sort_name")
    protected String sortName;

    /**
     * 排序方式
     */
    @ApiModelProperty(name = "sort_order",value = "分页 - 排序方式，asc/desc")
//    @JSONField(name = "sort_order")
    protected String sortOrder;

    @ApiModelProperty(name = "date", value = "起止日期")
    @JSONField(name = "date")
    protected List<String> dateRange;

//    @JSONField(name = "start_date")
    protected String startDate;

//    @JSONField(name = "end_date")
    protected String endDate;

    /**
     * 默认排序
     */
    protected String defaultOrder;

    /**
     * 排序字符串
     */
    protected String orderString;

    public String getOrderBy() {
        if (StringHelper.isNotBlank(this.sortName) && StringHelper.isNotBlank(this.sortOrder)) {
            String orderBy = this.sortName + " " + this.sortOrder;
            if (StringHelper.isNotBlank(this.defaultOrder) && !orderBy.equals(defaultOrder)) {
                orderBy += ", " + defaultOrder;
            }
            return orderBy.replaceAll(".*([';]+|(--)+).*", " ");
        }
        if (StringHelper.isNotBlank(this.defaultOrder)) {
            return defaultOrder.replaceAll(".*([';]+|(--)+).*", " ");
        }
        return null;
    }

}
