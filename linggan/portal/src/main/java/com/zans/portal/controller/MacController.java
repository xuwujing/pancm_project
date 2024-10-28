package com.zans.portal.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zans.base.annotion.Record;
import com.zans.base.office.excel.ExportConfig;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.TMacMapper;
import com.zans.portal.model.LogOperation;
import com.zans.portal.model.TMac;
import com.zans.portal.service.IFileService;
import com.zans.portal.service.ILogOperationService;
import com.zans.portal.service.IMacService;
import com.zans.portal.util.LogBuilder;
import com.zans.portal.vo.arp.ExcelMacVO;
import com.zans.portal.vo.mac.MacRespVO;
import com.zans.portal.vo.mac.MacVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.zans.base.config.EnumErrorCode.*;
import static com.zans.base.office.excel.ExportConfig.TOP;
import static com.zans.portal.config.GlobalConstants.*;
import static com.zans.portal.constants.PortalConstants.*;

@Api(value = "/mac", tags = {"/mac ~ mac来源管理"})
@RestController
@RequestMapping("/mac")
@Validated
@Slf4j
public class MacController extends BasePortalController {

    @Autowired
    IMacService macService;

    @Autowired
    TMacMapper macMapper;

    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    IFileService fileService;



    @Value("${api.export.folder}")
    String exportFolder;

    @ApiOperation(value = "/list", notes = "mac来源管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true, dataType = "MacVO", paramType = "body")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public ApiResult list(@RequestBody MacVO req) {
        super.checkPageParams(req, "id desc");

        PageResult<MacRespVO> pageResult = macService.getMacPage(req);

        Map<String, Object> result = MapBuilder.getBuilder()
                .put(INIT_DATA_TABLE, pageResult)
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/insertOrUpdate", notes = "新增mac来源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "req", value = "查询条件", required = true,
                    dataType = "MacRespVO", paramType = "body")
    })
    @RequestMapping(value = "insertOrUpdate", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_MAC,operation = PORTAL_LOG_OPERATION_ADD)
    public ApiResult insertOrUpdate(@Valid @RequestBody MacRespVO req, HttpServletRequest request) {
        int count = macService.getByMacAddress(req.getMacAddr(), req.getId());
        if (count > 0) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("mac地址已存在#" + req.getMacAddr());
        }
        TMac mac = new TMac();
        BeanUtils.copyProperties(req, mac);
        if (req.getId() == null) {
            macService.insert(mac);
        } else {
            macService.update(mac);
        }

        return ApiResult.success(MapBuilder.getSimpleMap("id", mac.getId())).appendMessage("请求成功");
    }

    @ApiOperation(value = "/delete", notes = "删除mac来源")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Record(module = PORTAL_MODULE_MAC,operation = PORTAL_LOG_OPERATION_ONE_DELETE)
    public ApiResult delete(@NotNull(message = "id必填") Integer id, HttpServletRequest request) {
        TMac mac = macService.getById(id);
        if (mac == null) {
            return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage("当前mac来源不存在#" + id);
        }
        macService.delete(mac);
        return ApiResult.success().appendMessage("删除成功");
    }


    @PostMapping("/upload")
    @ResponseBody
    @Record(module = PORTAL_MODULE_MAC,operation = PORTAL_LOG_OPERATION_IMPORT)
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {

//        if (!ExcelHelper.checkExtension(file)) {
//            return ApiResult.error(SERVER_UPLOAD_MIME_ERROR).appendMessage("不是Excel类型");
//        }
        long size = file.getSize();
        if (size > UPLOAD_FILE_MAX_SIZE) {
            return ApiResult.error(SERVER_UPLOAD_MAX_SIZE_ERROR)
                    .appendMessage("最大" + FileHelper.getSizeInMb(UPLOAD_FILE_MAX_SIZE) + "MB");
        }
        //读取上传文件的数据，然后格式化为map,存在的mac_addr数据以文件的company为准，不存在的新增
        Map<String, Object> macMap = readFile(file);
        try {
            save(macMap);
        }catch (SQLException e){
            log.error("SQL ERROR:",e);
            return ApiResult.error(SERVER_DB_ERROR);
        }catch (Exception e){
            log.error("ERROR:",e);
            return ApiResult.error(SERVER_LOGIC_ERROR);
        }
        return ApiResult.success();
    }


    @PostMapping("/export")
    @Record(module = PORTAL_MODULE_MAC,operation = PORTAL_LOG_OPERATION_EXPORT)
    public void downloadFile(@RequestBody MacVO req,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        log.info("downloadFile#{}", req);
        String filePath = this.exportFolder + "/" +
                "asset_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "mac地址来源";
        String fileCnName = name + "_" + DateHelper.getTodayShort() + ".xlsx";
        List<ExcelMacVO> list = macService.findMacList(req);
        Map<String, Object> contentMap = MapBuilder.getBuilder()
                .put("设备总数", list.size()).build();
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .extraContentPosition(TOP).extraContent(contentMap).extraContentBlankRow(1)
                .freeze(true).freezeCol(10).freezeRow(1 + contentMap.size() + 1).build();
        fileService.exportExcelFile(list, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }


    /**
     * @Author pancm
     * @Description 处理上传的数据
     * @Date  2020/7/30
     * @Param [macMap]
     * @return void
     **/
    private void save(Map<String, Object> macMap) throws Exception{
        Map<String, Object> updateMap = new HashMap<>();
        List<MacRespVO> list= macService.findMacListByMacAddr(macMap);
//        Map<String, Object> existMap = list.stream().collect(Collectors.toMap(MacRespVO::getMacAddr, MacRespVO::getCompany));
        Map<String, Object> existMap = Maps.newHashMap();
        for (MacRespVO macRespVO : list) {
            existMap.put(macRespVO.getMacAddr(),macRespVO.getCompany());
        }

        for (Map.Entry<String, Object> entry : existMap.entrySet()) {
            if (!entry.getValue().equals(macMap.get(entry.getKey()))) {
                if(macMap.get(entry.getKey())!=null){
                    updateMap.put(StringUtils.trimAllWhitespace(entry.getKey()),macMap.get(entry.getKey()));
//                    System.out.println("mac地址:"+entry.getKey()+" |t_mac表:"+entry.getValue()+" |oui的公司:"+macMap.get(entry.getKey()));
//                  try {
//                      macMapper.updateCompany(StringUtils.trimAllWhitespace(entry.getKey()), (String) macMap.get(entry.getKey()));
//                  }catch (Exception e){
//                      e.printStackTrace();
//                      System.out.println(StringUtils.trimAllWhitespace(entry.getKey())+"  "+(String) macMap.get(entry.getKey()));
//                  }
                }
            }
        }
        Map<String, Object> insertMap = getDifferenceMap(macMap,existMap);
//        System.out.println("t_mac没有的mac和公司:"+insertMap);
        if(updateMap!=null&&updateMap.size()>0){
             macService.batchUpdateCompany(updateMap);
        }
        if(insertMap!=null&&insertMap.size()>0){
             macService.insertBatch(insertMap);
        }
    }


    /**
     * @Author pancm
     * @Description 得到map的差集
     * @Date  2020/7/30
     * @Param [bigMap, smallMap]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    private  Map<String, Object> getDifferenceMap(Map<String, Object> bigMap, Map<String, Object> smallMap) {
        Set<String> bigMapKey = bigMap.keySet();
        Set<String> smallMapKey = smallMap.keySet();
        Set<String> differenceSet = Sets.difference(bigMapKey, smallMapKey);
        Map<String, Object> result = Maps.newHashMap();
        for (String key : differenceSet) {
            result.put(key, bigMap.get(key));
        }
        return result;
    }


    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author pancm
     * @Description 读取上传的文件通过切割符将数据转换成map
     * @Date 2020/7/30
     * @Param [file]
     **/
    private Map<String, Object> readFile(MultipartFile file) throws IOException {
        InputStream input = file.getInputStream();
        InputStreamReader reader = new InputStreamReader(input, "UTF-8");
        BufferedReader br = new BufferedReader(reader);
        String str = null;
        Map<String, Object> map = new HashMap<>();
        while ((str = br.readLine()) != null) {
            if (str.indexOf(SEPARATOR_HEX) > -1) {
                String mac = substringBefore(str, SEPARATOR_HEX).trim();
                String company = substringAfter(str, SEPARATOR_HEX).trim();
                map.put(mac, company);
            }
        }
        return map;
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取截取字符串后面的值
     * @Date 2020/7/30
     * @Param [str, separator]
     **/
    private String substringAfter(final String str, final String separator) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    /**
     * @return java.lang.String
     * @Author pancm
     * @Description 获取截取字符串前面的值
     * @Date 2020/7/30
     * @Param [str, separator]
     **/
    private String substringBefore(final String str, final String separator) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(0, pos);
    }





    private static final String SEPARATOR_HEX = "(hex)";

}
