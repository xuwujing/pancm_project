package com.zans.portal.vo.file;

import com.zans.base.office.excel.SheetEntity;
import com.zans.base.vo.SelectVO;
import lombok.Data;

import java.util.List;

/**
 * @author xv
 * @since 2020/3/20 14:45
 */
@Data
public class IpAllocFile {

    private String fileName;

    private Integer area;

    private String areaName;

    private Integer deviceType;

    private String deviceTypeName;

    private SheetEntity entity;

    /**
     * 是否校验通过
     */
    private Boolean valid;

    public void resetArea(List<SelectVO> list) {
        if (list == null) {
            return;
        }
        for (SelectVO vo : list) {
            vo.resetKey();
            if (fileName.contains(vo.getItemValue())) {
                this.area = (Integer) vo.getItemKey();
                break;
            }
        }
    }

    public void resetDeviceType(List<SelectVO> list) {
        if (list == null) {
            return;
        }
        for (SelectVO vo : list) {
            vo.resetKey();
            if (fileName.contains(vo.getItemValue())) {
                this.deviceType = (Integer) vo.getItemKey();
                break;
            }
        }
    }

}
