package com.zans.service;

import com.zans.model.DeviceCipher;
import com.zans.model.UserSession;
import com.zans.vo.ApiResult;
import com.zans.vo.DeviceCipherApproveVO;
import com.zans.vo.DeviceCipherRuleVO;
import com.zans.vo.DeviceCipherVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * (DeviceCipher)表服务接口
 *
 * @author beixing
 * @since 2021-08-23 16:15:53
 */
public interface IDeviceCipherService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceCipher queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceCipher 实例对象
     * @return 对象列表
     */
    ApiResult list(DeviceCipherVO deviceCipher);


    /**
     * 新增数据
     *
     * @param deviceCipher 实例对象
     * @return 实例对象
     */
    ApiResult insert(DeviceCipher deviceCipher, HttpServletRequest httpRequest);

    /**
     * 修改数据
     *
     * @param deviceCipher 实例对象
     * @return 实例对象
     */
    ApiResult update(DeviceCipher deviceCipher);

    ApiResult delete(Integer id);

    ApiResult importFile(MultipartFile file,HttpServletRequest request);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    void template(HttpServletRequest request, HttpServletResponse response);

    ApiResult viewPwd(String ip,HttpServletRequest request);

    ApiResult reboot(String ip);

    ApiResult approve(DeviceCipherApproveVO deviceCipherApproveVO,HttpServletRequest request);

    ApiResult password();

    ApiResult applyPwd(Integer id,HttpServletRequest request);

    ApiResult dashboard();

    ApiResult randomPwd();

    ApiResult deviceBrand();

}
