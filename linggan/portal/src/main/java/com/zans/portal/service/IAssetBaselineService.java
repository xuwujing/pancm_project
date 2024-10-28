package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.AssetBaseline;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.AssetBaselineVersionVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


/**
 * @author beixing
 * @Title: 基线表(AssetBaseline)表服务接口
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:09:40
 */
public interface IAssetBaselineService extends BaseService<AssetBaseline> {

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    AssetBaselineVO queryByIp(String ip);




    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetBaselineVO 实例对象
     * @return 对象列表
     */
    ApiResult list(AssetBaselineVO assetBaselineVO);


    /**
     * 新增数据
     *
     * @param assetBaselineVO 实例对象
     * @return 实例对象
     */
    int insertOne(AssetBaselineVO assetBaselineVO);

    /**
     * 修改数据
     *
     * @param assetBaselineVO 实例对象
     * @return 实例对象
     */
    int update(AssetBaselineVO assetBaselineVO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

	ApiResult historyList(String ip);

    ApiResult compareBaseLine(Long id);

    ApiResult historyBaselineList(AssetBaselineVersionVO assetBaselineVO);

    /**
    * @Author beiming
    * @Description
    * @Date  7/9/21
    * @Param
    * @return
    **/
    List<AssetBaseline> findByIpMac(String username, String curIpAddr);


    AssetBaselineVO getByIp(String ipAddr);

    List<AssetBaselineVO> getByMac(String mac);

    void unbindByIpAddr(String ip);

    ApiResult readExcel(MultipartFile file, InputStream inputStream, UserSession userSession);



    ApiResult excelTransform(String newFileName,  UserSession userSession);

    /**
     * 删除自动分配的IP
     * @param allocId
     * @return
     */
    int deleteIpByAlloc(Integer allocId);
}
