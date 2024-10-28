package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.RadiusEndpointMapper;
import com.zans.portal.dao.RadiusEndpointWhiteDao;
import com.zans.portal.model.RadiusEndpointWhite;
import com.zans.portal.service.IDeviceTypeService;
import com.zans.portal.service.IRadiusEndpointWhiteService;
import com.zans.portal.service.ISysConstantService;
import com.zans.portal.util.RestTemplateHelper;
import com.zans.portal.vo.RadiusEndpointWhiteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.INIT_DATA_TABLE;
import static com.zans.portal.config.GlobalConstants.MODULE_DEVICE;


/**
 * @author beixing
 * @Title: 设备白名单表(RadiusEndpointWhite)表服务实现类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-02-16 18:22:35
 */
@Service("radiusEndpointWhiteService")
@Slf4j
public class RadiusEndpointWhiteServiceImpl implements IRadiusEndpointWhiteService {
    @Resource
    private RadiusEndpointWhiteDao radiusEndpointWhiteDao;


    @Autowired
    private ISysConstantService constantService;

    @Autowired
    private RestTemplateHelper restTemplateHelper;

    @Autowired
    IDeviceTypeService deviceTypeService;
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public RadiusEndpointWhiteVO queryById(Long id) {
        return this.radiusEndpointWhiteDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(RadiusEndpointWhiteVO radiusEndpointWhite) {
        List<SelectVO> deviceList = deviceTypeService.findDeviceTypeToSelect();

        int pageNum = radiusEndpointWhite.getPageNum();
        int pageSize = radiusEndpointWhite.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RadiusEndpointWhiteVO> result = radiusEndpointWhiteDao.queryAll(radiusEndpointWhite);
        Map<String, Object> map = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, new PageResult<>(page.getTotal(), result, pageSize, pageNum))
                .put(MODULE_DEVICE, deviceList)
                .build();
        return ApiResult.success(map);

    }

    /**
     * 新增数据
     *
     * @param radiusEndpointWhiteVO 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(RadiusEndpointWhiteVO radiusEndpointWhiteVO) {
        RadiusEndpointWhite radiusEndpointWhite = new RadiusEndpointWhite();
        BeanUtils.copyProperties(radiusEndpointWhiteVO, radiusEndpointWhite);
        return radiusEndpointWhiteDao.insert(radiusEndpointWhite);
    }

    /**
     * 修改数据
     *
     * @param radiusEndpointWhiteVO 实例对象
     * @return 实例对象
     */
    @Override
    public int update(RadiusEndpointWhiteVO radiusEndpointWhiteVO) {
        RadiusEndpointWhite radiusEndpointWhite = new RadiusEndpointWhite();
        BeanUtils.copyProperties(radiusEndpointWhiteVO, radiusEndpointWhite);
        return radiusEndpointWhiteDao.update(radiusEndpointWhite);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.radiusEndpointWhiteDao.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult remove(RadiusEndpointWhiteVO radiusEndpointWhiteVO) {
        //1.删除记录
        //2.更新radius_endpoint 的白名单状态为0
        radiusEndpointWhiteDao.deleteById(radiusEndpointWhiteVO.getId());
//        endpointMapper.updateWhiteStatusByMac(radiusEndpointWhiteVO.getMac(),0);
        return ApiResult.success();
    }

    @Override
    public ApiResult refresh(RadiusEndpointWhiteVO radiusEndpointWhiteVO) {
        //调用judge_api的接口，进行再次数据刷新
        callJudgeApi(radiusEndpointWhiteVO.getMac());
        return ApiResult.success();
    }


    private ApiResult callJudgeApi(String mac) {
        String radApiHost = constantService.getJudgeApi();
        ApiResult result = null;
        try {
            String uri = "/api/radius/endpoint/aclWhite?mac=" + mac;
            String serverUrl = radApiHost + uri;
            log.info("reqUrl:{}", serverUrl);
            result = restTemplateHelper.getForObject(serverUrl, 60 * 1000);
            log.info("reqResult:{}", result);
            if (result == null) {
                return ApiResult.error("调用acl失败，请联系管理员");
            }
            return ApiResult.success(result);
        } catch (Exception e) {
            log.error("解析远程数据异常#", e);
            return ApiResult.error("解析远程数据异常#" + JSON.toJSONString(result));
        }
    }

    @Resource
    private RadiusEndpointMapper endpointMapper;
}
