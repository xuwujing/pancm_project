package com.zans.portal.service.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.DateHelper;
import com.zans.base.util.SpringContextHolder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.AssetBaselineAreaDao;
import com.zans.portal.dao.AssetBaselineDao;
import com.zans.portal.model.AssetBaseline;
import com.zans.portal.model.AssetBranch;
import com.zans.portal.service.IAssetBranchAssetService;
import com.zans.portal.service.IAssetBranchService;
import com.zans.portal.service.IDeviceTypeService;
import com.zans.portal.service.ISysConstantService;
import com.zans.portal.util.HttpHelper;
import com.zans.portal.vo.AssetBaselineAreaPageVO;
import com.zans.portal.vo.AssetBaselineAreaVO;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.asset.branch.req.AssetBranchAssetAddReqVO;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.zans.portal.config.GlobalConstants.DELETE_NOT;

/**
 * @author beixing
 * @Title: AssetDataListener
 * @Description: excel的监听器，用于处理excel的数据转换和存储
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/9/14
 **/

public class AssetDataListener extends AnalysisEventListener<Map<Integer, String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetDataListener.class);


    private ISysConstantService sysConstantService = SpringContextHolder.getBean(ISysConstantService.class);

    private AssetBaselineDao assetBaselineDao = SpringContextHolder.getBean(AssetBaselineDao.class);

    private AssetBaselineAreaDao assetBaselineAreaDao = SpringContextHolder.getBean(AssetBaselineAreaDao.class);


    private IAssetBranchService assetBranchService = SpringContextHolder.getBean(IAssetBranchService.class);
    private IAssetBranchAssetService assetBranchAssetService = SpringContextHolder.getBean(IAssetBranchAssetService.class);
    private IDeviceTypeService deviceTypeService = SpringContextHolder.getBean(IDeviceTypeService.class);


    protected HttpHelper httpHelper = SpringContextHolder.getBean(HttpHelper.class);


    private static Map<String, AssetBaselineAreaVO> areaVOMap = null;


    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private List<AssetBaseline> list = new ArrayList<>();

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        SelectVO selectVO = sysConstantService.findSelectVOByKey(GlobalConstants.BASE_IMPORT_EXCEL_JSON);
        SelectVO selectVO2 = sysConstantService.findSelectVOByKey(GlobalConstants.PROJECT_ACTIVE);
        JSONObject jsonObject = JSON.parseObject(selectVO.getItemValue());
        UserSession session = getUserSession();
        AssetBaseline newAsset = new AssetBaseline();
//        if (GlobalConstants.PROJECT_ENV_XYGA.equals(selectVO2.getItemValue())) {
        setAssetBaseLine(data, jsonObject, newAsset);
//        } else {
//            newAsset = JSON.parseObject(JSONObject.toJSONString(data), AssetBaseline.class);
//        }

        String ip = newAsset.getIpAddr();
        AssetBaselineAreaVO assetBaselineAreaVO = assetBaselineAreaDao.queryByIp(ip);
        if (assetBaselineAreaVO != null) {
            int level = 3;
            int parentId = 1;
            String areaName = assetBaselineAreaVO.getAreaName();
            Integer baselineAreaId = assetBaselineAreaVO.getId();
            newAsset.setAreaNameLevel3(areaName);
            AssetBaselineAreaVO assetBaselineAreaVO2 = assetBaselineAreaDao.queryByAreaName(areaName);
            if (assetBaselineAreaVO2 != null) {
                String areaNameLevel2 = assetBaselineAreaVO2.getAreaName();
                Integer baselineAreaId2 = assetBaselineAreaVO2.getId();
                newAsset.setAreaNameLevel2(areaNameLevel2);
                AssetBaselineAreaVO assetBaselineAreaVO1 = assetBaselineAreaDao.queryByAreaName(areaNameLevel2);
                if (assetBaselineAreaVO1 != null) {
                    String areaNameLevel1 = assetBaselineAreaVO1.getAreaName();
                    Integer baselineAreaId1 = assetBaselineAreaVO1.getId();
                    level = 1;
                    parentId = createAssetGroup(areaNameLevel1, baselineAreaId1, session, level, 0);
                    newAsset.setAreaNameLevel1(areaNameLevel1);
                }
                level = 2;
                parentId = createAssetGroup(areaNameLevel2, baselineAreaId2, session, level, parentId);
            }
            newAsset.setCreator(session.getUserName());
            newAsset.setReviser(session.getUserName());
            //写入基线表
            saveData(newAsset);
            level = 3;
            //创建资产分组
            int assetGroupId = createAssetGroup(areaName, baselineAreaId, session, level, parentId);
            //添加资产
            AssetBranchAssetAddReqVO req = new AssetBranchAssetAddReqVO();
            req.setAssetBranchId(assetGroupId);
            req.setIpAddr(ip);
            assetBranchAssetService.groupsAddAsset(req, session);
        } else {
            LOGGER.warn("ip:{},在asset_baseline_area表中并未查询到！直接写入基线表!", ip);
            newAsset.setCreator(session.getUserName());
            newAsset.setReviser(session.getUserName());
            //写入基线表
            saveData(newAsset);
        }


//        list.add(newAsset);
//        if (list.size() >= BATCH_COUNT) {
//            saveData(list);
//            list.clear();
//        }
    }


    private UserSession getUserSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) {
            return null;
        }
        return httpHelper.getUser(request);
    }


//    private int createAssetGroup(String areaName, UserSession session) {
//        Integer count = assetBranchService.findByName(areaName, null);
//        if (count == null || count == 0) {
//            Integer nextSeq = assetBranchService.getNextSeq();
//            AssetBranch assetBranch = new AssetBranch();
//            assetBranch.setName(areaName);
//            assetBranch.setDeleteStatus(DELETE_NOT);
//            assetBranch.setSeq(nextSeq);
//            assetBranch.setCreatorId(session.getUserId());
//            assetBranch.setUpdateId(session.getUserId());
//            assetBranchService.saveSelective(assetBranch);
//            count = assetBranch.getId();
//        }
//        return count;
//    }

    private int createAssetGroup(String areaName, Integer baselineAreaId, UserSession session, Integer level, Integer parentId) {
        Integer count = assetBranchService.findByName(areaName, null);
        if (count == null || count == 0) {
            Integer nextSeq = assetBranchService.getNextSeq(parentId);
            AssetBranch assetBranch = new AssetBranch();
            assetBranch.setName(areaName);
            assetBranch.setBaselineAreaId(baselineAreaId);
            assetBranch.setDeleteStatus(DELETE_NOT);
            assetBranch.setSeq(nextSeq);
            assetBranch.setLevel(level);
            assetBranch.setParentId(parentId);
            assetBranch.setCreatorId(session.getUserId());
            assetBranch.setUpdateId(session.getUserId());
            assetBranchService.saveSelective(assetBranch);
            count = assetBranch.getId();
        }
        return count;
    }


    private void setAssetBaseLine(Map<Integer, String> data, JSONObject jsonObject, AssetBaseline newAsset) {
        String file = "fileName";
        for (Object key : jsonObject.keySet()) {
            String fileName = jsonObject.getJSONObject(key.toString()).getString(file);
            String value = data.get(Integer.valueOf(key.toString()));
            try {
                BeanUtils.copyProperty(newAsset, fileName, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("键值设置失败！data:{},jsonObject:{},fileName:{},value:{}", data, jsonObject, fileName, value);
            }
        }
    }

    private void getAreaMap() {
        if (areaVOMap == null) {
            List<AssetBaselineAreaVO> areaVOList = assetBaselineAreaDao.queryAll(new AssetBaselineAreaPageVO());
            if (!StringHelper.isEmpty(areaVOList)) {
                areaVOList.forEach(assetBaselineAreaVO -> {
                    areaVOMap.put(assetBaselineAreaVO.getAreaName(), assetBaselineAreaVO);
                });
            } else {
                areaVOMap = new HashMap<>();
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(AssetBaseline baseline) {
        if(!StringUtils.isEmpty(baseline.getDeviceTypeName())){
           Integer deviceType = deviceTypeService.findTypeByName(baseline.getDeviceTypeName());
            if(deviceType!=null){
                baseline.setDeviceType(deviceType);
            }else {
                LOGGER.warn("该设备名称:{},未进行配置！请检查t_device_type表的设备类型配置！",baseline.getDeviceTypeName());
            }
        }
        if(!StringUtils.isEmpty(baseline.getMaintainStatusName())){
            String mn = baseline.getMaintainStatusName();
            baseline.setMaintainStatus(getMaintainStatus(mn));
        }

        AssetBaselineVO assetBaselineVO = assetBaselineDao.getByIp(baseline.getIpAddr());
        if (assetBaselineVO == null) {
            baseline.setCreateTime(DateHelper.getDateTime(new Date()));
            assetBaselineDao.insertSelective(baseline);
            return;
        }
        baseline.setId(assetBaselineVO.getId());
        assetBaselineDao.update(baseline);
    }

    private int getMaintainStatus(String name){
        int status =1;
        if("迁改停用".equals(name)){
            status =2;
        }
        if("审核停用".equals(name)){
            status =3;
        }
        return status;
    }


    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        LOGGER.error("解析失败:{}", exception);
        throw exception;
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
//        if (exception instanceof ExcelDataConvertException) {
//            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
//            LOGGER.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
//                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
//        }
    }
}
