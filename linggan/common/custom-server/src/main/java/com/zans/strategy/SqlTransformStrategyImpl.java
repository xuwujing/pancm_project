package com.zans.strategy;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.dao.SysCustomFieldConfigDao;
import com.zans.dao.SysPropertyFieldDao;
import com.zans.dao.SysQueryFieldConfigDao;
import com.zans.model.SysCustomFieldConfig;
import com.zans.model.SysPropertyField;
import com.zans.vo.ApiResult;
import com.zans.vo.CustomReqVO;
import com.zans.vo.PageResult;
import com.zans.vo.SelectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author beixing
 * @Title: custom-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/4/19
 */
@Service
@Slf4j
public class SqlTransformStrategyImpl implements ISqlTransformStrategy {

    @Resource
    private SysPropertyFieldDao propertyFieldDao;

    @Resource
    private SysCustomFieldConfigDao sysCustomFieldConfigDao;

    @Resource
    private SysQueryFieldConfigDao sysQueryFieldConfigDao;

    @Override
    public String getSql(QueryReqVO queryReqVO) {
        String property = queryReqVO.getProperty();
        List<SysPropertyField> list = propertyFieldDao.queryAll(new SysPropertyField());
        //进行sql拼接
        StringBuffer sb = new StringBuffer();
        for (SysPropertyField propertyField : list) {
            String p = propertyField.getName();
            if (p.equals(property)) {
                int status = propertyField.getFiledStatus();
                String[] values = queryReqVO.getValues();
                String fieldKey = queryReqVO.getKey();
                String fieldValue = propertyField.getValue();
                //0 就是 等于、大于和小于
                if (status == 0) {
                    sb.append(" and ").append(fieldKey).append(" ").append(fieldValue).append(" '").append(values[0]).append("' ");
                    return sb.toString();
                }
                // 1 就是like
                if (status == 1) {
                    sb.append(" and ").append(fieldKey).append(" ").append(fieldValue).append(" '%").append(values[0]).append("%' ");
                    return sb.toString();
                }

                //10 就是 key是空
                if (status == 10) {
                    sb.append(" and ").append(fieldKey).append(" ").append(fieldValue);
                    return sb.toString();
                }

                //20 就是 相邻，必须拥有两个值
                if (status == 20) {
                    sb.append(" and ").append(fieldKey).append(" ").append(fieldValue).append(" '")
                            .append(values[0]).append("' and '").append(values[1]).append("'");
                    return sb.toString();
                }
                //30 就是 in或not in，需要遍历
                if (status == 30) {
                    sb.append(" and ").append(fieldKey).append(" ").append(fieldValue).append(" (");
                    for (int i = 0; i < values.length; i++) {
                        sb.append("'").append(values[i]).append("'");
                        if(i<values.length-1){
                            sb.append(",");
                        }
                    }
                    sb.append(") ");
                    return sb.toString();
                }
            }
        }

        return null;
    }

    @Override
    public ApiResult init(String module) {
        SysCustomFieldConfig sysCustomFieldConfig = sysCustomFieldConfigDao.queryByModule(module);
        List<QueryRespVO> queryFieldConfigs = sysQueryFieldConfigDao.queryByModule(module);

        List<SelectVO> mapList = propertyFieldDao.init();
        String[] sqlHeads = sysCustomFieldConfig.getSqlHead().split(",");
        String[] sqlHeadEns = sysCustomFieldConfig.getSqlHeadEn().split(",");
        List<ColumnRespVO> columnRespVOList = new LinkedList<>();
//        Map<String, String> data = new LinkedHashMap<>();
        for (int i = 0; i < sqlHeads.length; i++) {
//            data.put(sqlHeads[i], sqlHeadEns[i]);
            ColumnRespVO columnRespVO = new ColumnRespVO();
            columnRespVO.setItemKey(sqlHeads[i]);
            columnRespVO.setItemValue(sqlHeadEns[i]);
            columnRespVOList.add(columnRespVO);
        }

        Map<String, Object> result = new HashMap<>();
        result.put(CUSTOM_COLUMN, columnRespVOList);
        result.put(CUSTOM_QUERY, queryFieldConfigs);
        result.put(CUSTOM_PROPERTY, mapList);
        return ApiResult.success(result);
    }

    @Override
    public ApiResult query(CustomReqVO reqVO) {
        String module = reqVO.getModule();
        SysCustomFieldConfig sysCustomFieldConfig = sysCustomFieldConfigDao.queryByModule(module);
        String[] sqlHeads = sysCustomFieldConfig.getSqlHead().split(",");
        String[] sqlHeadEns = sysCustomFieldConfig.getSqlHeadEn().split(",");
        Map<String, String> data = new LinkedHashMap<>();
        for (int i = 0; i < sqlHeads.length; i++) {
            data.put(sqlHeads[i], sqlHeadEns[i]);
        }

        String sql = sysCustomFieldConfig.getSqlText();
        Map<String, QueryReqVO> map = reqVO.getMap();
        for (String key : map.keySet()) {
            QueryReqVO queryReqVO = map.get(key);
            String jointSql = getSql(queryReqVO);
            sql = sql.concat(jointSql);
        }
        log.info("查询sql:{}",sql);
        int pageNum = reqVO.getPageNum();
        int pageSize = reqVO.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<JSONObject> list = sysCustomFieldConfigDao.executeSql(sql);
        PageResult pageResult = new PageResult(page.getTotal(), list, pageSize, pageNum);
        return ApiResult.success(pageResult);
    }


    /**
     * 检疫区自定义列
     */
    public static String CUSTOM_COLUMN = "customColumn";

    /**
     * 查询项
     */
    public static String CUSTOM_QUERY = "customQuery";

    /**
     * 字段属性
     */
    public static String CUSTOM_PROPERTY = "customProperty";







}
