package com.zans.portal.dao;

import com.zans.portal.vo.alert.*;
import com.zans.portal.vo.asset.guardline.resp.AssetGuardLineStrategyRespVO;
import com.zans.portal.vo.chart.CountUnit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: portal
 * @Description: 告警规则dao实现
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/27
 */
@Repository
public interface AlertRuleMapper {

    List<AlertRecordRespVO> getAlertRecord(AlertRecordSearchVO reqVO);

    /**
     * @Author pancm
     * @Description 分开查询，这里查询交换机相关信息的数据
     * @Date  2020/11/19
     * @Param [reqVO]
     * @return java.util.List<com.zans.portal.vo.alert.AlertRecordRespVO>
     **/
    List<AlertRecordRespVO> getAlertRecord2(AlertRecordSearchVO reqVO);


    List<AlertRecordRespVO> getAlertRecordTop(AlertRecordSearchVO reqVO);

    List<AlertRecordRespVO> getAlertRecordHis(AlertRecordSearchVO reqVO);

    AlertRecordRespVO getAlertRecordView(Long id);

    AlertRecordRespVO getAlertRecordView2(Long id);


    /**
    * @Author beixing
    * @Description  查询一条数据
    * @Date  2021/10/18
    * @Param
    * @return
    **/
    AlertRecordRespVO getAlertRecordByReq(AlertRecordSearchVO reqVO);


    List<AlertIpClashRespVO> getAlertIpClash(String businessId);

    AlertRecordRespVO getAlertRecordOriginalView(Long id);


    /**
     * @Author pancm
     * @Description 查看详情
     * @Date  2020/12/9
     * @Param [businessId]
     * @return com.zans.portal.vo.alert.AlertDetailRespVO
     **/
    AlertDetailRespVO getAlertRecordOriginalDetailView(String businessId);


    /**
     * @Author pancm
     * @Description
     * @Date
     * @Param [reqVO]
     * @return java.util.List<com.zans.portal.vo.alert.AlertRecordRespVO>
     **/
    List<AlertRecordRespVO> getAlertRecordDispose(AlertRecordSearchVO reqVO);

    List<AlertRecordRespVO> getAlertRecordDispose2(AlertRecordSearchVO reqVO);


    List<Long> getAlertRecordDisposeByKeywordValue(String keywordValue);

    /**
     * 查询所有报过异常的IP地址
     */
    List<String> getAlertRecordIpAddr();


    /**
     * @Author pancm
     * @Description 获取告警类型报表
     * @Date  2020/9/16
     * @Param []
     * @return com.zans.portal.vo.alert.AlertReportRespVO
     **/
    List<AlertReportRespVO> getAlertRecordTypeReport(@Param("status") Integer status);

    List<AlertReportDisRespVO> getAlertRecordDisTypeReport();

    List<AlertReportDisRespVO> getAlertRecordType();

    List<AlertReportDisRespVO> getAlertRecordType2();

    List<AlertReportDisRespVO> getAlertRecordRuleType();


    List<AlertReportRespVO> getAlertRecordRule();


    /**
     * @Author pancm
     * @Description 获取告警等级报表
     * @Date  2020/9/16
     * @Param []
     * @return com.zans.portal.vo.alert.AlertReportRespVO
     **/
    List<AlertReportRespVO> getAlertRecordLevelReport();



    /**
     * @Author pancm
     * @Description 获取告警处理总数
     * @Date  2020/9/16
     * @Param []
     * @return com.zans.portal.vo.alert.AlertReportRespVO
     **/
    Long getAlertRecordDealCount();


    /**
     * @Author pancm
     * @Description 获取告警未处理总数
     * @Date  2020/9/16
     * @Param []
     * @return java.lang.Long
     **/
    Long getAlertRecordNotDealCount();


    void updateRuleRecord(AlertRecordSearchVO reqVO);

    void delRecordByBId(String businessId);

    void delOriginalByBId(String businessId);


    void delTriggerByBId(String businessId);

    AlertRecordRespVO getRecordById(Long id);

    AlertRecordRespVO getRecordByBId(String businessId);

    void insertRecordDel(AlertRecordRespVO save);


    void batchDelRecordByIds(Map<String, Object> ids);



    List<AlertRuleRespVO> getAlertRule(AlertRuleSearchVO reqVO);

    List<AlertIndexRespVO> getAlertIndex(AlertIndexSearchVO reqVO);

    List<AlertTypeRespVO> getAlertTypePage(AlertTypeSearchVO reqVO);

    List<AlertTypeRespVO> getAlertType(AlertTypeSearchVO reqVO);

    List<AlertIndexRespVO> getAlertIndex2(@Param("typeId") Integer typeId);

    List<AlertRecordRespVO> getAlertRuleName(@Param("typeId") Integer typeId);



    List<String> getRecordByKeywordValues(Map<String,Object> kvs);


    List<String> getRecordByIp(Map<String,Object> kvs);

    /**
     * @Author pancm
     * @Description 通过mac查询radius_acct表
     * @Date  2020/11/30
     * @Param [mac]
     * @return com.zans.portal.vo.alert.AlertRecordRespVO
     **/
    AlertRecordRespVO getRecordByMac(String mac);

    CountUnit findAlertTotalByDate(@Param("date") String date);

    CountUnit findAlertDealByDate(@Param("date") String date);

    List<CountUnit> getAlertUnReadTotal();




    @Select("${sqlStr}")
    void executeSql(@Param(value = "sqlStr") String sqlStr);


    int batchUpdateByIds(Map<String, Object> ids);


	List<AssetGuardLineStrategyRespVO> guardline(Long assetGuardLineId);

	Integer checkGroupName(@Param("id") Long id,@Param("groupName") String groupName);

	void readRecordById(@Param("id") Long id);

	List<AlertRecordRespVO> getHisAlertRecord(AlertRecordSearchVO reqVO);

    List<AlertRecordRespVO> getHisAlertRecord2(AlertRecordSearchVO reqVO);

    /**
    * @Author beixing
    * @Description  告警的设备类型统计
    * @Date  2021/12/15
    * @Param
    * @return
    **/
    List<AlertRecordRespVO> getAlertRecordDeviceType(@Param("ruleId") Long ruleId);

    /**
    * @Author beixing
    * @Description 查询处理和未处理的
    * @Date  2021/12/16
    * @Param
    * @return
    **/
    List<AlertReportDisRespVO> getAlertRecordDealCountAndNotCount();

}
