package com.zans.controller;

import com.zans.dao.SysConstantItemDao;
import com.zans.dao.SysPropertyFieldDao;
import com.zans.vo.ApiResult;
import com.zans.vo.SelectVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.strategy.SqlTransformStrategyImpl.CUSTOM_PROPERTY;

@RestController
@RequestMapping("/base")
@Slf4j
public class BaseController {

    @Resource
    private SysPropertyFieldDao propertyFieldDao;

    @ApiOperation(value = "/init", notes = "初始化")
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    public ApiResult init(HttpServletRequest request) {
        String module = "t_user_list";
        List<String> stringList = sysConstantItemDao.queryAllByKey();
        Map<String, Object> result = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        for (int i = 0; i < stringList.size(); i++) {
            String key = stringList.get(i);
            List<SelectVO> selectVOS = sysConstantItemDao.queryByKey(key);
            map.put(key,selectVOS);
        }
        List<SelectVO> mapList = propertyFieldDao.init();
        result.put("dict",map);
        result.put(CUSTOM_PROPERTY,mapList);
        return ApiResult.success(result);
    }





    @Resource
    private SysConstantItemDao sysConstantItemDao;

}
