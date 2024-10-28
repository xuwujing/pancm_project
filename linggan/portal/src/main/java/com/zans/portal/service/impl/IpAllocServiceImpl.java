package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.exception.RollbackException;
import com.zans.base.office.excel.SheetEntity;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.IpAllocMapper;
import com.zans.portal.model.AssetBaseline;
import com.zans.portal.model.IpAlloc;
import com.zans.portal.service.*;
import com.zans.portal.vo.file.IpAllocFile;
import com.zans.portal.vo.ip.ExcelIpAssignVO;
import com.zans.portal.vo.ip.IpAllocRespVO;
import com.zans.portal.vo.ip.IpAllocSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.*;

/**
 * @author xv
 * @since 2020/3/24 13:46
 */
@Service
@Slf4j
public class IpAllocServiceImpl extends BaseServiceImpl<IpAlloc> implements IIpAllocService {

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IFileService fileService;

    @Autowired
    IAssetBaselineService assetBaselineService;

//    @Autowired
//    IIpService ipService;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    IAssetService assetService;

    @Value("${api.upload.folder}")
    String uploadFolder;

    IpAllocMapper allocMapper;

    @Resource
    public void setAllocMapper(IpAllocMapper allocMapper) {
        super.setBaseMapper(allocMapper);
        this.allocMapper = allocMapper;
    }

    @Override
    public PageResult<IpAllocRespVO> getIpAllocPage(IpAllocSearchVO req) {
        int pageNum = req.getPageNum();
        int pageSize = req.getPageSize();
        String orderBy = req.getSortOrder();

        Page page = PageHelper.startPage(pageNum, pageSize, orderBy);
        List<IpAllocRespVO> list = allocMapper.findAllIpAlloc(req);
        Map<Object, String> nameMap = constantItemService.findItemsMapByDict(MODULE_IP_ALLOC_INSERT_STATUS);
        Map<Object, String> validMap = constantItemService.findItemsMapByDict(MODULE_IP_ALLOC_VALID_STATUS);
        for(IpAllocRespVO ip : list) {
            ip.setInsertStatusNameByMap(nameMap);
            ip.setValidStatusNameByMap(validMap);
        }
        return new PageResult<IpAllocRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public IpAllocRespVO getIpAlloc(Integer id) {
        IpAllocRespVO vo = allocMapper.findIpAlloc(id);
        Map<Object, String> nameMap = constantItemService.findItemsMapByDict(MODULE_IP_ALLOC_INSERT_STATUS);
        Map<Object, String> validMap = constantItemService.findItemsMapByDict(MODULE_IP_ALLOC_VALID_STATUS);
        if (vo != null) {
            vo.setInsertStatusNameByMap(nameMap);
            vo.setValidStatusNameByMap(validMap);
        }
        return vo;
    }

    @Override
    public List<Object> readIpAllocFile(String filePath, String fileName) {
        File file = this.getRemoteFile(filePath, fileName);
        if (file == null) {
            return null;
        }
        log.info("file#{}", file.getAbsolutePath());
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            IpAllocFile allocFile = fileService.readIpAllocFile(fileName, is);
            if (allocFile == null) {
                return null;
            }
            SheetEntity sheetEntity = allocFile.getEntity();
            if (sheetEntity == null) {
                return null;
            }
            return sheetEntity.convertToRawTable(sheetEntity.getHeaderClass());
        } catch (Exception ex) {
            log.error("读取IP映射文件失败#" + filePath, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex1) {
                log.error("readIpAllocFile error, close error", ex1);
            }
        }
        return null;
    }



    @Override
    public Triplet<Boolean, List<Object>, String> checkIpAllocFile(String filePath, String fileName, String newFilePath) {
        Triplet<Boolean, List<Object>, String> result = new Triplet<>(false, new ArrayList<>(0), "");

        File file = this.getRemoteFile(filePath, fileName);
        if (file == null) {
            throw new RuntimeException("文件不存在#" + filePath);
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            String absoluteNewFilePath = this.uploadFolder + "/" + newFilePath;
            IpAllocFile allocFile = fileService.checkFile(fileName, is, absoluteNewFilePath);
            log.info("after check#{}", allocFile);
            if (allocFile == null) {
                return result;
            }
            SheetEntity sheetEntity = allocFile.getEntity();
            if (sheetEntity == null) {
                return result;
            }
            List<Object> list = sheetEntity.convertToRawTable(sheetEntity.getHeaderClass());
            log.info("after check#{}", list);
            result = new Triplet<>(sheetEntity.getValid(), list, "");
        } catch (Exception ex) {
            String errorMessage = "校验文件失败#" + filePath;
            log.error(errorMessage, ex);
            result = new Triplet<>(false, new ArrayList<>(0), errorMessage + ", " + ex.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex1) {
                log.error("readIpAllocFile error, close error", ex1);
            }
        }
        return result;
    }

    @Override
    public Triplet<Boolean, List<Object>, String> checkIpAllocFileIp(String filePath, String fileName, String newFilePath) {
        Triplet<Boolean, List<Object>, String> result = new Triplet<>(false, new ArrayList<>(0), "");
        File file = this.getRemoteFile(filePath, fileName);
        if (file == null) {
            throw new RuntimeException("文件不存在#" + filePath);
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            String absoluteNewFilePath = this.uploadFolder + "/" + newFilePath;
            IpAllocFile allocFile = fileService.checkFileIp(fileName, is, absoluteNewFilePath);
            log.info("after check#{}", allocFile);
            if (allocFile == null) {
                return result;
            }
            SheetEntity sheetEntity = allocFile.getEntity();
            if (sheetEntity == null) {
                return result;
            }
            List<Object> list = sheetEntity.convertToRawTable(sheetEntity.getHeaderClass());
            log.info("after check#{}", list);
            result = new Triplet<>(sheetEntity.getValid(), list, "");
        } catch (Exception ex) {
            String errorMessage = "校验文件失败#" + filePath;
            log.error(errorMessage, ex);
            result = new Triplet<>(false, new ArrayList<>(0), errorMessage + ", " + ex.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex1) {
                log.error("readIpAllocFile error, close error", ex1);
            }
        }
        return result;
    }


    private File getRemoteFile(String filePath, String fileName) {
        if (filePath == null || fileName == null) {
            return null;
        }
        File file = new File(this.uploadFolder + "/" + filePath);
        if (!file.exists()) {
            log.error("file error！ file does not exist！ #" + this.uploadFolder + "/" + filePath);
            return null;
        }
        return file;
    }



    @Override
    public Triplet<Boolean, List<Object>, String> assignIpAlloc(IpAlloc ipAlloc, String filePath, String fileName, String newFilePath) {
        Triplet<Boolean, List<Object>, String> result = new Triplet<>(false, new ArrayList<>(0), "");

        File file = this.getRemoteFile(filePath, fileName);
        if (file == null) {
            return result;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            String absoluteNewFilePath = this.uploadFolder + "/" + newFilePath;
            log.info("after check#{}", absoluteNewFilePath);
            IpAllocFile allocFile = fileService.assignIp(ipAlloc, fileName, is, absoluteNewFilePath);
            if (allocFile == null) {
                return result;
            }
            SheetEntity sheetEntity = allocFile.getEntity();
            if (sheetEntity == null) {
                return result;
            }
            List<Object> list = sheetEntity.convertToRawTable(sheetEntity.getHeaderClass());
            result = new Triplet<>(sheetEntity.getValid(), list, "");
        } catch (Exception ex) {
            String errorMessage = "分配IP失败#" + filePath;
            log.error(errorMessage, ex);
            result = new Triplet<>(false, new ArrayList<>(0),
                    errorMessage + ", " + ex.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex1) {
                log.error("readIpAllocFile error, close error", ex1);
            }
        }
        return result;
    }

    /**
     * 手动分配IP，分配结果写入新文件，不写数据库
     *
     * @param ipAlloc
     * @param filePath    文件在服务器的相对路径
     * @param fileName    文件名，中文
     * @param newFilePath 新文件的服务器的相对路径
     * @return (boolean, 列表, 错误原因)
     */
    @Override
    public Triplet<Boolean, List<Object>, String> handAssignIpAlloc(IpAlloc ipAlloc, String filePath, String fileName, String newFilePath) {
        //todo 测试使用
       if(1==1){
           return new Triplet<>(true, new ArrayList<>(0), "");
       }

        Triplet<Boolean, List<Object>, String> result = new Triplet<>(false, new ArrayList<>(0), "");

        File file = this.getRemoteFile(filePath, fileName);
        if (file == null) {
            return result;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            String absoluteNewFilePath = this.uploadFolder + "/" + newFilePath;
            log.info("after check#{}", absoluteNewFilePath);
            IpAllocFile allocFile = fileService.handAssignIp(ipAlloc, fileName, is, absoluteNewFilePath);
            if (allocFile == null) {
                log.error("ip分配失败！", absoluteNewFilePath);
                return result;
            }
            SheetEntity sheetEntity = allocFile.getEntity();
            if (sheetEntity == null) {
                return result;
            }
            List<Object> list = sheetEntity.convertToRawTable(sheetEntity.getHeaderClass());
            result = new Triplet<>(sheetEntity.getValid(), list, "");
        } catch (Exception ex) {
            String errorMessage = "分配IP失败#" + filePath;
            log.error(errorMessage, ex);
            result = new Triplet<>(false, new ArrayList<>(0),
                    errorMessage + ", " + ex.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex1) {
                log.error("readIpAllocFile error, close error", ex1);
            }
        }
        return result;
    }

    @Transactional(rollbackFor = RollbackException.class)
    @Override
    public boolean importAllocIp(IpAlloc ipAlloc, List<Object> table, UserSession user) {
        boolean result = true;
        Date today = new Date();
        String deviceTypeName = deviceTypeService.getNameByType(ipAlloc.getDeviceType().toString());
        for (Object obj : table) {
            if (obj instanceof ExcelIpAssignVO) {
                ExcelIpAssignVO vo = (ExcelIpAssignVO) obj;
                AssetBaseline baseline = new AssetBaseline();
                // ipAddr, gateway, vlan, mask, device_model_brand, device_model_des
                BeanUtils.copyProperties(vo, baseline);
                baseline.setDeviceTypeName(deviceTypeName);
                // 类型不一致
                if (vo.getVlan() != null) {
                    baseline.setVlan(vo.getVlan() + "");
                }
                // 使用 统一的 company、project、联系人、联系电话
                baseline.setContractor(ipAlloc.getContractor());
                baseline.setProjectName(ipAlloc.getProjectName());
                baseline.setDeviceType(ipAlloc.getDeviceType());
                baseline.setAreaId(ipAlloc.getAreaId());
                baseline.setContractor(ipAlloc.getContractor());
                baseline.setContractorPerson(ipAlloc.getContractorPerson());
                baseline.setContractorPhone(ipAlloc.getContractorPhone());
                baseline.setAllocId(ipAlloc.getId());
                baseline.setCreator(user.getUserName());
                baseline.setReviser(user.getUserName());
                this.assetBaselineService.save(baseline);
            } else {
                result = false;
                log.error("importAllocIp error , unknown class#" + obj.getClass());
                break;
            }
        }
        if (result) {
            ipAlloc.setAllocDay(today);
            String newFileName = this.generateFileName(ipAlloc);
            ipAlloc.setFileName(newFileName);
            ipAlloc.setInsertStatus(STATUS_ENABLE);
            this.update(ipAlloc);
        }
        return result;
    }

    @Override
    public boolean isIpAlloc(Integer id) {
        Long count = allocMapper.isIpAlloc(id);
        return count > 0;
    }

    @Override
    public String generateFileName(IpAlloc ipAlloc) {
        String today = DateHelper.getTodayShort();
        String areaName = areaService.getAreaNameById(ipAlloc.getAreaId());
        String deviceTypeName = deviceTypeService.getNameByType(ipAlloc.getDeviceType().toString());
        return String.format("%s%s_%s_%s_%s.xlsx", deviceTypeName, today, areaName,
                ipAlloc.getProjectName(), ipAlloc.getContractor());
    }

    @Override
    public String generateFileName(IpAllocRespVO vo) {
        String today = DateHelper.getTodayShort();
        return String.format("%s%s_%s_%s_%s.xlsx", vo.getDeviceTypeName(), today, vo.getAreaName(),
                vo.getProjectName(), vo.getContractor());
    }

    @Override
    public String generateFileByTemplate(IpAllocRespVO vo) {
        String fileName = this.generateFileName(vo);
        String newFilePath = fileService.getNewFileName(fileName);
        File file = this.getRemoteFile(vo.getTemplate(), fileName);
        if (file == null) {
            return null;
        }

        // 清空
        Map<String, Object> valueMap = MapBuilder.getBuilder()
                .put("projectName", vo.getProjectName())
                .put("areaName", vo.getAreaName())
                .put("company", vo.getContractor())
                .put("ipAddr", null)
                .put("gateway", null)
                .put("mask", null)
                .put("vlan", null).build();

        InputStream is = null;
        try {
            is = new FileInputStream(file);
            String absoluteNewFilePath = this.uploadFolder + "/" + newFilePath;
            return fileService.generateFileByTemplate(fileName, is, absoluteNewFilePath, valueMap);
        } catch (Exception ex) {
            String errorMessage = "设置模板失败#" + vo.getTemplate();
            log.error(errorMessage, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex1) {
                log.error("readIpAllocFile error, close error", ex1);
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public boolean deleteIpAlloc(IpAlloc ipAlloc, UserSession userSession) {
        if (ipAlloc == null || userSession == null) {
            return false;
        }

        assetBaselineService.deleteIpByAlloc(ipAlloc.getId());

        String oldFilePath = ipAlloc.getFilePath();
        String content = JSON.toJSONString(ipAlloc);
        this.delete(ipAlloc);

        FileHelper.deleteFile(this.uploadFolder, oldFilePath);

        // 记录日志
//        LogOperation logOperation = new LogBuilder().session(userSession)
//                .module(LOG_MODULE_IP_ALLOC)
//                .operation(LOG_OPERATION_DELETE)
//                .content(content)
//                .result(LOG_RESULT_SUCCESS).build();
//        logOperationService.save(logOperation);

        return true;
    }


    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public boolean deleteIpAlloc2(IpAlloc ipAlloc, UserSession userSession) {
        if (ipAlloc == null || userSession == null) {
            return false;
        }

        assetBaselineService.deleteIpByAlloc(ipAlloc.getId());

        String oldFilePath = ipAlloc.getFilePath();
        String content = JSON.toJSONString(ipAlloc);
        FileHelper.deleteFile(this.uploadFolder, oldFilePath);

        // 记录日志
//        LogOperation logOperation = new LogBuilder().session(userSession)
//                .module(LOG_MODULE_IP_ALLOC)
//                .operation(LOG_OPERATION_DELETE)
//                .content(content)
//                .result(LOG_RESULT_SUCCESS).build();
//        logOperationService.save(logOperation);

        return true;
    }
}
