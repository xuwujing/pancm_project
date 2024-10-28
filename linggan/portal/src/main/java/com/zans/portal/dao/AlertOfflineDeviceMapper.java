package com.zans.portal.dao;

import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceDisposeVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceResVO;
import com.zans.portal.vo.alert.offlineDevice.OfflineDeviceSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author beihai
 *
 */
@Repository
public interface AlertOfflineDeviceMapper {

    List<OfflineDeviceResVO> getAlertOfflineDevicePage(@Param("reqVo") OfflineDeviceSearchVO reqVO);

    Integer countOfflineDeviceByTime(@Param("aliveLastTime")Date aliveLastTime,@Param("isLessThan")boolean isLessThan);

    List<OfflineDeviceResVO> getByIds(List<Integer> ids);

    @Deprecated
    int dispose(OfflineDeviceDisposeVO disposeVO);

}
