package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.model.IpAlloc;
import com.zans.portal.vo.ip.IpAllocRespVO;
import com.zans.portal.vo.ip.IpAllocSearchVO;
import org.javatuples.Triplet;

import java.util.List;

/**
 * @author xv
 * @since 2020/3/24 13:46
 */
public interface IIpAllocService extends BaseService<IpAlloc> {

    /**
     * IP分配，列表查询
     * @param reqVO 查询条件
     * @return 列表
     */
    PageResult<IpAllocRespVO> getIpAllocPage(IpAllocSearchVO reqVO);

    /**
     * IP分配，查询单个记录
     * @param id 记录主键
     * @return 记录
     */
    IpAllocRespVO getIpAlloc(Integer id);

    /**
     * 读取文件的数据，返回行列
     * @param filePath 文件在服务器的相对路径
     * @param fileName 文件名，中文
     * @return
     */
    List<Object> readIpAllocFile(String filePath, String fileName);

    /**
     * 校验文件，校验结果写入新文件
     * @param filePath 文件在服务器的相对路径
     * @param fileName 文件名，中文
     * @param newFilePath 新文件的服务器的相对路径
     * @return (boolean, 列表, 错误原因)
     */
    Triplet<Boolean, List<Object>, String> checkIpAllocFile(String filePath, String fileName, String newFilePath);

    /**
     * 校验文件中的ip是否合法，校验结果写入新文件
     * @param filePath 文件在服务器的相对路径
     * @param fileName 文件名，中文
     * @param newFilePath 新文件的服务器的相对路径
     * @return (boolean, 列表, 错误原因)
     */
    Triplet<Boolean, List<Object>, String> checkIpAllocFileIp(String filePath, String fileName, String newFilePath);

    /**
     * 分配IP，分配结果写入新文件，不写数据库
     * @param ipAlloc
     * @param filePath 文件在服务器的相对路径
     * @param fileName 文件名，中文
     * @param newFilePath 新文件的服务器的相对路径
     * @return (boolean, 列表, 错误原因)
     */
    Triplet<Boolean, List<Object>, String> assignIpAlloc(IpAlloc ipAlloc, String filePath, String fileName, String newFilePath);


    /**
     * 手动分配IP，分配结果写入新文件，不写数据库
     * @param ipAlloc
     * @param filePath 文件在服务器的相对路径
     * @param fileName 文件名，中文
     * @param newFilePath 新文件的服务器的相对路径
     * @return (boolean, 列表, 错误原因)
     */
    Triplet<Boolean, List<Object>, String> handAssignIpAlloc(IpAlloc ipAlloc, String filePath, String fileName, String newFilePath);



    /**
     * 自动分批的IP，导入 t_ip_all 里，更新t_ip_alloc
     * @param ipAlloc IP分配信息
     * @param table IP分配列表
     * @param user user session
     * @return
     */
    boolean importAllocIp(IpAlloc ipAlloc, List<Object> table, UserSession user);

    /**
     * 上述IP是否已经分配
     * @param id
     * @return
     */
    boolean isIpAlloc(Integer id);

    String generateFileName(IpAlloc ipAlloc);

    String generateFileName(IpAllocRespVO ipAlloc);
    /**
     * 根据标准模板，设置area、deviceType、projectName、company，contractor字段
     * @param vo IP分配信息，有
     * @return
     */
    String generateFileByTemplate(IpAllocRespVO vo);

    boolean deleteIpAlloc(IpAlloc ipAlloc, UserSession userSession);

    boolean deleteIpAlloc2(IpAlloc ipAlloc, UserSession userSession);
}
