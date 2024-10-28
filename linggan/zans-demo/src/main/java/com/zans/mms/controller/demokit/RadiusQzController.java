package com.zans.mms.controller.demokit;


import com.zans.base.controller.BaseController;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.mms.dao.guard.AlertRuleMapper;
import com.zans.mms.model.Area;
import com.zans.mms.model.DeviceType;
import com.zans.mms.model.Universality;
import com.zans.mms.service.*;
import com.zans.mms.vo.alert.AlertRecordRespVO;
import com.zans.mms.vo.alert.AlertRecordSearchVO;
import com.zans.mms.vo.radius.QzRespVO;
import com.zans.mms.vo.radius.QzViewRespVO;
import com.zans.mms.vo.switcher.SwitcherVlanConfigRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.CLIENT_PARAMS_ERROR;


@Api(value = "demoKit/radius/qz", tags = {"demoKit/radius/qz ~ radius 隔离区"})
@RestController
@RequestMapping("demoKit/radius/qz")
@Slf4j
public class RadiusQzController extends BaseController {

    @Autowired
    IIpService ipService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IArpService arpService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IRadiusEndPointService radiusEndPointService;

    @Autowired
    private AlertRuleMapper mapper;

    @Autowired
    IAssetService iAssetService;

    @Autowired
    ISysSwitcherVlanConfigService sysSwitcherVlanConfigService;

    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author pancm
     * @Description 获取IP和资产对比信息数据
     * @Date 2020/9/28
     * @Param [respVO]
     **/
    public Map<String, Object> getIpAndAssetsCp(QzRespVO respVO) {
        List<QzViewRespVO> ipView = new ArrayList<>();
        List<QzViewRespVO> arpView = new ArrayList<>();
        //校验对比的数据
        List<Universality> verifyView = new ArrayList<>();

        //2020-10-29 和北辰确认ip信息对比查询由t_ip_all改为查询asset表
        //2020-11-6 如果 t_ip_all表的数据为空，那么就查询asset
        //Ip对比
        QzViewRespVO ipRes = new QzViewRespVO();
        ipRes = ipService.findByIp(respVO.getIpAddr());
        if (ipRes == null) {
            ipRes = iAssetService.findByIp(respVO.getIpAddr());
        }
        QzViewRespVO qzRes = new QzViewRespVO();
        if (ipRes == null) {
            ipRes = new QzViewRespVO();
            qzRes.setIpStatus(0);
            ipRes.setIpStatus(0);
            respVO.setIsIpNotAllocErr(1);
            Universality universality = new Universality();
            universality.setName("ip未分配");
            universality.setValue(5);
            verifyView.add(universality);
        }

        //2020-12-16 列表页增加vlan配置错误的提示
        String vlan = ipRes.getVlan();
        String curVlan = respVO.getVlan();
        if (!StringUtils.isEmpty(vlan) && !StringUtils.isEmpty(curVlan)) {
            if (!vlan.equals(curVlan)) {
                respVO.setIsVlanErr(1);
                Universality universality = new Universality();
                universality.setName("Vlan错误");
                universality.setValue(1);
                verifyView.add(universality);
            }
        }
        if (StringUtils.isEmpty(ipRes.getVlan())) {
            ipRes.setVlan("-");
        }

        String areaName = ipRes.getAreaName();
        String curArea = respVO.getAreaName();
        if (!StringUtils.isEmpty(areaName) && !StringUtils.isEmpty(curArea)) {
            if (!areaName.equals(curArea)) {
                respVO.setIsAreaErr(1);
                Universality universality = new Universality();
                universality.setName("所属辖区");
                universality.setValue(2);
                verifyView.add(universality);
            }
        }

        String osName = ipRes.getServerOs();
        String curOsName = respVO.getAreaName();
        if (!StringUtils.isEmpty(osName) && !StringUtils.isEmpty(curOsName)) {
            if (!osName.equals(curOsName)) {
                respVO.setIsOsErr(1);
                Universality universality = new Universality();
                universality.setName("设备操作类型");
                universality.setValue(3);
                verifyView.add(universality);
            }
        }

        String modelDes = ipRes.getDeviceModelDes();
        String curModelDes = respVO.getModelDes();
        String brandName = ipRes.getDeviceModelBrand();
        String curBrandName = respVO.getBrandName();
        final boolean isModelDesOrBrandNameErr = (!StringUtils.isEmpty(modelDes) && !StringUtils.isEmpty(curModelDes))
                || (!StringUtils.isEmpty(brandName) && !StringUtils.isEmpty(curBrandName));
        if (isModelDesOrBrandNameErr) {
            if ((!StringUtils.isEmpty(modelDes) && !modelDes.equals(curModelDes)) || (!StringUtils.isEmpty(brandName) && !brandName.equals(curBrandName))) {
                respVO.setIsModelDesErr(1);
                Universality universality = new Universality();
                universality.setName("设备型号");
                universality.setValue(4);
                verifyView.add(universality);
            }
        }


        BeanUtils.copyProperties(respVO, qzRes);
        qzRes.setModuleName("扫描设备信息");
        qzRes.setMac(respVO.getUsername());
        qzRes.setDeviceModelDes(respVO.getModelDes());
        qzRes.setCurOpenPort(respVO.getOpenPortAll());
        qzRes.setDeviceModelBrand(respVO.getBrandName());
        qzRes.setPointName(ipRes.getPointName());
        qzRes.setProjectName(ipRes.getProjectName());
        qzRes.setMaintainCompany(ipRes.getMaintainCompany());
        qzRes.setContractorPerson(ipRes.getContractorPerson());
        qzRes.setContractorPhone(ipRes.getContractorPhone());
        qzRes.setVlan(ipRes.getVlan());
        Integer deviceType = respVO.getDeviceType();
        if (deviceType != null && deviceType != 0) {
            DeviceType device = deviceTypeService.getById(deviceType);
            if (device != null) {
                qzRes.setTypeName(device.getTypeName());
            }
        }

        //2020-9-11 增加逻辑，deviceType如果在上述条件查不到，将ipRes的数据放到
        if (deviceType == null && qzRes.getTypeName() == null) {
            qzRes.setTypeName(ipRes.getTypeName());
            qzRes.setDeviceModelDes(ipRes.getTypeName());
        }

        Integer areaId = respVO.getAreaId();
        if (areaId != null && areaId != 0) {
            Area area = areaService.getById(areaId);
            if (area != null) {
                qzRes.setAreaName(area.getAreaName());
            }
        }
        // 2020-10-26 修改这个ip资产对比的顺序
        ipView.add(ipRes);
        ipView.add(qzRes);

        //资产对比
        // 2020-9-27 资产对比由t_arp表改为radius_endpoint_profile
        QzViewRespVO curRqs = radiusEndPointService.findCurByCurMac(respVO.getUsername());
        QzViewRespVO baseRqs = radiusEndPointService.findBaseByCurMac(respVO.getUsername());
        if (curRqs == null) {
            curRqs = new QzViewRespVO();
            curRqs.setArpStatus(0);
        } else {
            curRqs.setNasIp(respVO.getNasIp());
            curRqs.setNasName(respVO.getNasName());
            curRqs.setNasPort(respVO.getNasPort());
            curRqs.setNasPortType(respVO.getNasPortType());
            curRqs.setNasPortId(respVO.getNasPortId());
            curRqs.setCalledStationId(respVO.getCalledStationId());
        }

        if (baseRqs == null) {
            baseRqs = new QzViewRespVO();
            baseRqs.setArpStatus(0);
        }
        arpView.add(curRqs);
        arpView.add(baseRqs);
        if (!StringUtils.isEmpty(respVO.getIpAddr())) {
            AlertRecordSearchVO reqVO = new AlertRecordSearchVO();
            reqVO.setIpAddr(respVO.getIpAddr());
            reqVO.setIndexId(12);
            List<AlertRecordRespVO> list = mapper.getAlertRecord(reqVO);
            reqVO.setIndexId(17);
            List<AlertRecordRespVO> list2 = mapper.getAlertRecord(reqVO);
            if ((list != null && list.size() > 0) || (list2 != null && list2.size() > 0)) {
                Universality universality = new Universality();
                universality.setName("ip冲突");
                universality.setValue(6);
                verifyView.add(universality);
            }
        }

        //交换机配置的 VLAN 地址范围(当前ip应该在哪个交换机哪个VLAN上)
        List<SwitcherVlanConfigRespVO> vlanView = sysSwitcherVlanConfigService.getIpMatchVlanList(respVO.getIpAddr());
        if (vlanView == null) {
            vlanView = new ArrayList<>(0);
        }
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("ipView", ipView)
                .put("arpView", arpView)
                .put("vlanView", vlanView)
                .put("verifyView", verifyView)
                .build();
        return map;
    }


    @ApiOperation(value = "/view", notes = "隔离区详情")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/view", method = {RequestMethod.GET})
    public ApiResult<QzRespVO> view(@NotNull(message = "id必填") Integer id) {

        QzRespVO respVO = radiusEndPointService.findQzById(id);
        if (respVO == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("隔离区不存在#" + id);
        }
        String nasPortId = respVO.getNasPortId();
        if (!StringUtils.isEmpty(nasPortId)) {
            nasPortId = nasPortId.replace("3D", "");
            nasPortId = nasPortId.replace("=3B", ";");
            nasPortId = getPortId(nasPortId);
            respVO.setNasPortId(nasPortId);
        }

        Map<String, Object> resultMap = getIpAndAssetsCp(respVO);
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("radiusQz", respVO)
                .build();
        map.putAll(resultMap);
        return ApiResult.success(map);
    }

    private String getPortId(String nasPortId) {
        String[] portIds = nasPortId.split(";");
        if (portIds.length < 3) {
            return nasPortId;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            String sp = portIds[i];
            if (i > 0) {
                sb.append("/");
            }
            sb.append(substringAfter(sp, "="));
        }
        return sb.toString();
    }


    private String substringAfter(final String str, final String separator) {
        if (org.springframework.util.StringUtils.isEmpty(str)) {
            return "";
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }
}
