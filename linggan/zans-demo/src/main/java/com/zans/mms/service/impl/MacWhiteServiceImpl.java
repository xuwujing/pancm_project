package com.zans.mms.service.impl;

import com.zans.base.vo.ApiResult;
import com.zans.mms.dao.mms.MacWhiteMapper;
import com.zans.mms.service.IMacWhiteService;
import com.zans.mms.util.NullPageNumAndSizeUtil;
import com.zans.mms.util.ZansDateUtils;
import com.zans.mms.vo.MacWhiteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author qiyi
 * @Title: zans-demo
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/28
 */
@Slf4j
@Service
public class MacWhiteServiceImpl implements IMacWhiteService {

    @Autowired
    private MacWhiteMapper macWhiteMapper;


    @Override
    public ApiResult selectAllMac(MacWhiteVO macWhiteVO) {
        List<MacWhiteVO> macWhiteVOS = macWhiteMapper.selectAllMac(macWhiteVO);
        return ApiResult.success(macWhiteVOS);
    }

    @Override
    public ApiResult insertMac(MacWhiteVO macWhiteVO) {
        if (!StringUtils.hasText(macWhiteVO.getUsername())){
            return ApiResult.error("mac地址不能为空");
        }
        List<MacWhiteVO> macWhites = macWhiteMapper.selectAllMac(new MacWhiteVO());
        for (MacWhiteVO macWhite : macWhites) {
            if (macWhite.getUsername().equalsIgnoreCase(macWhiteVO.getUsername())){
                return ApiResult.error("mac地址不能重复");
            }
        }
        macWhiteVO.setCreateTime(ZansDateUtils.now());
        macWhiteVO.setUpdateTime(ZansDateUtils.now());
        return ApiResult.success(macWhiteMapper.insertMac(macWhiteVO) == 1 ? "添加成功":"添加失败");
    }

    @Override
    public ApiResult delMac(String ids) {
        List<String> finalIds = Arrays.asList(ids.split(","));
        return ApiResult.success(macWhiteMapper.delMac(finalIds));
    }
}
