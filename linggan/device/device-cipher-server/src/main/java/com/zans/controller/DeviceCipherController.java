package com.zans.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.dao.DeviceCipherDao;
import com.zans.model.DeviceCipher;
import com.zans.model.UserSession;
import com.zans.service.IDeviceCipherService;
import com.zans.service.IFileService;
import com.zans.utils.DateHelper;
import com.zans.utils.HttpHelper;
import com.zans.utils.StringHelper;
import com.zans.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.zans.vo.ExportConfig.TOP;


/**
 * (DeviceCipher)表控制层
 *
 * @author beixing
 * @since 2021-08-23 16:15:54
 */
@RestController
@RequestMapping("deviceCipher")
@Slf4j
public class DeviceCipherController extends BaseController {
    /**
     * 服务对象
     */
    @Autowired
    private IDeviceCipherService deviceCipherService;

    @Resource
    DeviceCipherDao deviceCipherDao;

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    IFileService fileService;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Value("${api.export.folder}")
    String exportFolder;

    /**
     * 新增一条数据
     *
     * @param deviceCipher 实体类
     * @return Response对象
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResult insert(@RequestBody DeviceCipher deviceCipher, HttpServletRequest httpRequest) {
        return deviceCipherService.insert(deviceCipher,httpRequest);
    }

    @RequestMapping(value = "data", method = RequestMethod.GET)
    public ApiResult deviceBrand() {
        return deviceCipherService.deviceBrand();
    }

    /**
     * 修改一条数据
     *
     * @param deviceCipher 实体类
     * @return Response对象
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ApiResult update(@RequestBody DeviceCipher deviceCipher, HttpServletRequest httpRequest) {
        return deviceCipherService.update(deviceCipher);
    }

    /**
     * 删除一条数据
     *
     * @return Response对象
     */
    @RequestMapping(value = "del", method = RequestMethod.GET)
    public ApiResult delete(@RequestParam("id") Integer id, HttpServletRequest httpRequest) {
        return deviceCipherService.delete(id);
    }


    /**
     * 分页查询
     */
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ApiResult<DeviceCipher> list(@RequestBody DeviceCipherVO deviceCipher) {
        return deviceCipherService.list(deviceCipher);
    }

    /**
     * 导入
     *
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public ApiResult importFile(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {
        return deviceCipherService.importFile(file,request);
    }

    /**
     * 下载模板
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "template", method = RequestMethod.GET)
    public void template(HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        String filePath = this.uploadFolder + "设备模板.xlsx";
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileCnName = "设备模板" + date + ".xlsx";
        this.download(filePath, fileCnName, request, response);
    }

    /**
     * 导出
     *
     * @param deviceCiphervo
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public void export(@RequestBody DeviceCipherVO deviceCiphervo,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        log.info("downloadFile#{}", deviceCiphervo);
        String filePath = this.exportFolder +
                "device_" + DateHelper.getTodayShort() + "_" + StringHelper.getUuid() + ".xlsx";
        String name = "设备信息";
        String fileCnName = name + "_" + DateHelper.getTodayShort() + ".xlsx";
        Page<DeviceCipher> page = PageHelper.startPage(1, 20);
        List<DeviceCipher> deviceCiphers = deviceCipherDao.queryAll(deviceCiphervo);
        List<DeviceCipherExcel> finalData = new ArrayList<>();
        for (DeviceCipher deviceCipher : deviceCiphers) {
            DeviceCipherExcel deviceCipherExcel = new DeviceCipherExcel();
            BeanUtils.copyProperties(deviceCipher, deviceCipherExcel);
            deviceCipherExcel.setAliveName(deviceCipher.getAliveStatus() == 0 ? "离线" : "在线");
            deviceCipherExcel.setIsValidName(deviceCipher.getIsValid() == 0 ? "不正确" : "正确");
            deviceCipherExcel.setIsStrongName(deviceCipher.getIsStrong() == 0 ? "弱口令" : "强口令");
            deviceCipherExcel.setDeleteStatusName(deviceCipher.getDeleteStatus() == 0 ? "未删除" : "已删除");
            finalData.add(deviceCipherExcel);
        }
        ExportConfig exportConfig = ExportConfig.builder()
                .seqColumn(true)
                .wrap(true)
                .extraContentPosition(TOP).extraContent(new HashMap<>()).extraContentBlankRow(1)
                .freeze(true).freezeCol(10).freezeRow(2).build();
        fileService.exportExcelFile(finalData, name, filePath, exportConfig);
        this.download(filePath, fileCnName, request, response);
    }

    /**
     * 查看密码
     *
     * @param ip
     * @param request
     * @return
     */
    @RequestMapping(value = "password/view", method = RequestMethod.GET)
    public ApiResult viewPwd(@RequestParam("ip") String ip, HttpServletRequest request) {
        return deviceCipherService.viewPwd(ip, request);
    }

    /**
     * 申请查看密码
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "applyPwd", method = RequestMethod.GET)
    public ApiResult applyPwd(@RequestParam("id") Integer id, HttpServletRequest request) {
        return deviceCipherService.applyPwd(id, request);
    }

    @RequestMapping(value = "reboot", method = RequestMethod.GET)
    public ApiResult reboot(@RequestParam("ip") String ip) {
        return deviceCipherService.reboot(ip);
    }

    /**
     * 审核 查看密码的 申请
     *
     * @param deviceCipherApproveVO
     * @return
     */
    @RequestMapping(value = "approve", method = RequestMethod.POST)
    public ApiResult approve(@RequestBody DeviceCipherApproveVO deviceCipherApproveVO, HttpServletRequest request) {
        return deviceCipherService.approve(deviceCipherApproveVO, request);
    }

    /**
     * 密码规则回显
     *
     * @return
     */
    @RequestMapping(value = "password/rule", method = RequestMethod.GET)
    public ApiResult password() {
        return deviceCipherService.password();
    }

    /**
     * 控制台数据
     * @return
     */
    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public ApiResult dashboard() {
        return deviceCipherService.dashboard();
    }

    /**
     * 随机密码
     * @return
     */
    @RequestMapping(value = "randomPwd", method = RequestMethod.GET)
    public ApiResult randomPwd() {
        return deviceCipherService.randomPwd();
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }

}
