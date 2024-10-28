package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.VulHostVuln;
import com.zans.portal.vo.AlertRuleStrategyReqVO;
import com.zans.portal.vo.alert.*;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: portal
 * @Description: 告警查询service
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/27
 */
public interface IAlertRuleService {

    PageResult<AlertRecordRespVO> getAlertRecordPage(AlertRecordSearchVO reqVO);

    /**
     * @Author pancm
     * @Description 处置界面初始化
     * @Date
     * @Param [status]
     * @return com.zans.base.vo.ApiResult
     **/
    ApiResult  getAlertRecordInit(int status);


    ApiResult  getAlertRecordRuleInit();


    ApiResult  getAlertRecordRecent();




    PageResult<AlertRecordRespVO> getAlertRecordPageTop(AlertRecordSearchVO reqVO);


    PageResult<AlertRecordRespVO> getAlertRecordHisPage(AlertRecordSearchVO reqVO);

    ApiResult getAlertRecordView(Long id, int typeId);


    ApiResult getAlertRecordViewOtherDetail(VulHostVuln vulHostVuln);






    ApiResult  getAlertUnReadTotal();


    /**
     * @Author pancm
     * @Description 处置界面初始化
     * @Date
     * @Param [status]
     * @return com.zans.base.vo.ApiResult
     **/
    ApiResult  getAlertRecordDisposeInit();


    PageResult<AlertRecordRespVO> getAlertRecordDisposePage(AlertRecordSearchVO reqVO);


    void updateRuleRecord(AlertRecordSearchVO reqVO);

    void delRecordById(Long id);

    void batchDelRecordByIds(String ids);

    void cleanRecordByKeyword(String keywordValue);


    PageResult<AlertRuleRespVO> getAlertRulePage(AlertRuleSearchVO reqVO);

    PageResult<AlertTypeRespVO> getAlertTypePage(AlertTypeSearchVO reqVO);

    List<SelectVO> getAlertType();

    List<SelectVO> getAlertIndex(Integer typeId);

    List<SelectVO> getAlertRuleName(Integer typeId);



    PageResult<AlertIndexRespVO> getAlertIndexPage(AlertIndexSearchVO reqVO);

    List<String> getRecordByKeywordValues(Map<String, Object> kvs);

    List<String> getRecordByIp(Map<String, Object> kvs);

    /**
     * 获取所有出现过异常的IP
     * @return
     */
    List<String> getHasAlertIpAddr();


    /**
    * @Author beiming
    * @Description  调用远程告警消除
    * @Date  7/12/21
    * @Param
    * @return
    **/
    ApiResult remoteAlertRecordRecover(AlertRecordReqVO alertRecordReqVO);


	ApiResult guardline(Long assetGuardLineId);

    ApiResult addStrategy( AlertRuleStrategyReqVO alertRuleStrategyReqVO);

	ApiResult updateStatus(AlertRuleStrategyReqVO alertRuleStrategyReqVO);

    Boolean checkGroupName(Long id,String groupName);

    /**
     * 告警设为已读
     * @param id
     */
	void readRecordById(Long id);

    /**
     * 批量告警设为已读
     * @param ids
     */
    void batchReadRecordByIds(String ids);

    /**
     * 历史风险列表
     * @param req
     * @return
     */
    PageResult<AlertRecordRespVO> getHisAlertRecordPage(AlertRecordSearchVO req);

    ApiResult addFromVul(AddAlertForVulVO vulVO);

    /**
     * @Author beiming
     * @Description  调用远程告警消除
     * @Date  7/12/21
     * @Param
     * @return
     **/
    ApiResult addAlertRecordRecover(AlertRecordReqVO alertRecordReqVO);

}
