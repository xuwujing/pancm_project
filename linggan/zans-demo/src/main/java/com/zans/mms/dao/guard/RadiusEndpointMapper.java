package com.zans.mms.dao.guard;


import com.zans.mms.model.RadiusEndpoint;
import com.zans.mms.model.RadiusEndpointLog;
import com.zans.mms.vo.arp.AssetRespVO;
import com.zans.mms.vo.arp.AssetSearchVO;
import com.zans.mms.vo.radius.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RadiusEndpointMapper extends Mapper<RadiusEndpoint> {

    List<EndPointViewVO> findEndPointList(@Param("reqVo") EndPointReqVO reqVO);


    List<EndPointViewVO> findBlockEndPointList(@Param("reqVo") EndPointReqVO reqVO);

    void saveRadiusEndpointLog(RadiusEndpointLog radiusEndpointLog);

    List<AssetRespVO> findAssetsForList(@Param("asset") AssetSearchVO asset);

    /**
     * @Author pancm
     * @Description 新增一个通过pass查询设备信息，告警中心使用
     * @Date  2020/9/9
     * @Param [reqVO]
     * @return com.zans.portal.vo.radius.EndPointViewVO
     **/
    EndPointViewVO findEndPointByPass(@Param("pass") String pass);

    QzRespVO findQzById(Integer id);

    QzViewRespVO findBaseByCurMac(@Param("username")  String username);

    QzViewRespVO findCurByCurMac(@Param("username")  String username);

    RadiusPolicyRespVO getPolicy(String username);

	void deleteById(Integer id);

	void deleteByEndpoint(String mac,String ip);

    String getByEndPointId(Integer id);

    void initData();

}
