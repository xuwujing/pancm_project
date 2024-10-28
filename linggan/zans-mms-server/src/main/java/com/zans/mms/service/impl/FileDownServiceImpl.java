package com.zans.mms.service.impl;


import com.zans.base.util.FileHelper;
import com.zans.base.util.ImageHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.mms.dao.BaseVfsDao;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.service.IFileDownService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * @author pancm
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/1/21
 */
@Service("fileDownService")
@Slf4j
public class FileDownServiceImpl implements IFileDownService {

    @Autowired
    private BaseVfsDao baseVfsDao;

    @Value("${api.imgUrl.folder}")
    private String imgUrl;

    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;


    private final String raw = "raw";

    private final String thumbnail = "thumbnail";

    @Override
    public void image(String adjunctUuid, Integer type, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("adjunctUuid", adjunctUuid);
        }};
    }


    @Override
    public ApiResult upLoadImg(String adjunctId,MultipartFile[] files,Integer type,String module,String number) {
        log.info("附件编号:{} 开始上传:{} 张图片!", adjunctId,files.length);
        Map<String,Object> result = new HashMap<>();
        if (files != null && files.length > 0) {
            if(StringUtils.isEmpty(adjunctId)){
                adjunctId = StringHelper.getUuid();
            }
            BaseVfs baseVfs;
            try {
                for (MultipartFile file : files) {
                    baseVfs = new BaseVfs();
                    String uuid = StringHelper.getUuid();
                    String newFileName = number+"_"+uuid +"."+ FileHelper.getFileExtension(file.getOriginalFilename());
                    baseVfs.setImgType(type);
                    baseVfs.setAdjunctId(adjunctId);
                    //2021/8/2进行原图压缩 设短边为720像素 长边自适应
                    byte[] compress = ImageHelper.compressImage(file.getBytes(), true);
                    String rawFileName = getPathName(raw,module);
                    String thumbnailFileName = getPathName(thumbnail,module);
                    //进行上传图片到指定路径
                    FileHelper.saveFile(file, imgUrl+File.separator+rawFileName, newFileName);
                    FileHelper.saveFile(compress, imgUrl+File.separator+thumbnailFileName, newFileName);
                    baseVfs.setRawFilePath(rawFileName+ File.separator+newFileName);
                    baseVfs.setThumbnailFilePath(thumbnailFileName+ File.separator+newFileName);
                    baseVfs.setContentType(file.getContentType());
                    //2021/8/2 为了图片放在上传图片的最后，默认排序为integer的最大值
                    baseVfs.setSort(Integer.MAX_VALUE);
                    baseVfsDao.insertSelective(baseVfs);
                }
            } catch (IOException e) {
                log.error("图片上传失败！",e);
                return  ApiResult.error("图片上传失败！");
            }
            log.info("附件编号:{} 添加:{} 张图片成功!", adjunctId,files.length);
            result.put("adjunctId",adjunctId);
        }else {
            log.error("附件编号:{} 添加:{} 张图片失败!", adjunctId,files.length);
            return  ApiResult.error("请选择图片！");
        }
        return ApiResult.success(result);
    }

    @Override
    public ApiResult pcUpLoadImg(String adjunctId, MultipartFile[] files, Integer type, String module, String number) {
        log.info("附件编号:{} 开始上传:{} 张图片!", adjunctId,files.length);
        Map<String,Object> result = new HashMap<>();
        if (files != null && files.length > 0) {
            if(StringUtils.isEmpty(adjunctId)){
                adjunctId = StringHelper.getUuid();
            }
            BaseVfs baseVfs;
            try {
                for (MultipartFile file : files) {
                    baseVfs = new BaseVfs();
                    String uuid = StringHelper.getUuid();
                    String newFileName = number+"_"+uuid +"."+ FileHelper.getFileExtension(file.getOriginalFilename());
                    baseVfs.setImgType(type);
                    baseVfs.setAdjunctId(adjunctId);
                    //2021/8/2进行原图压缩 设短边为720像素 长边自适应
                    byte[] originalCompress = ImageHelper.compressOrigincalImage(file.getBytes(), true);
                    byte[] compress = ImageHelper.compressImage(file.getBytes(), true);
                    String rawFileName = getPathName(raw,module);
                    String thumbnailFileName = getPathName(thumbnail,module);
                    //进行上传图片到指定路径
                    FileHelper.saveFile(originalCompress, imgUrl+File.separator+rawFileName, newFileName);
                    FileHelper.saveFile(compress, imgUrl+File.separator+thumbnailFileName, newFileName);
                    baseVfs.setRawFilePath(rawFileName+ File.separator+newFileName);
                    baseVfs.setThumbnailFilePath(thumbnailFileName+ File.separator+newFileName);
                    baseVfs.setContentType(file.getContentType());
                    //2021/8/2 为了图片放在上传图片的最后，默认排序为integer的最大值
                    baseVfs.setSort(Integer.MAX_VALUE);
                    baseVfsDao.insertSelective(baseVfs);
                }
            } catch (IOException e) {
                log.error("图片上传失败！",e);
                return  ApiResult.error("图片上传失败！");
            }
            log.info("附件编号:{} 添加:{} 张图片成功!", adjunctId,files.length);
            result.put("adjunctId",adjunctId);
        }else {
            log.error("附件编号:{} 添加:{} 张图片失败!", adjunctId,files.length);
            return  ApiResult.error("请选择图片！");
        }
        return ApiResult.success(result);
    }

    @Override
    public ApiResult delImg(String adjunctId, Long id) {
        log.info("ID:{},附件编号:{} 进行删除！", id,adjunctId);
        baseVfsDao.deleteById(id);
        return ApiResult.success();
    }


    private String getPathName(String prefix,String module){
        LocalDateTime localDateTime= LocalDateTime.now();
        int yyyy = localDateTime.getYear();
        int mm = localDateTime.getMonthValue();
        int dd = localDateTime.getDayOfMonth();
        return prefix + File.separator+ yyyy+File.separator+mm+File.separator+dd +File.separator +module;
    }


}
