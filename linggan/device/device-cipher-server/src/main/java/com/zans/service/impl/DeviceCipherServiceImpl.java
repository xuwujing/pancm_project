package com.zans.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.dao.*;
import com.zans.model.DeviceCipher;
import com.zans.model.DeviceCipherApprove;
import com.zans.model.*;
import com.zans.service.IDeviceCipherService;
import com.zans.service.IFileService;
import com.zans.utils.AESUtil;
import com.zans.utils.FileHelper;
import com.zans.utils.HttpClientUtil;
import com.zans.utils.HttpHelper;
import com.zans.vo.*;
import com.zans.vo.excel.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static com.zans.config.GlobalConstants.UPLOAD_FILE_MAX_SIZE;
import static com.zans.constant.EnumErrorCode.*;
import static com.zans.constant.SystemConstant.*;


/**
 * (DeviceCipher)表服务实现类
 *
 * @author beixing
 * @since 2021-08-23 16:15:53
 */
@Slf4j
@Service("deviceCipherService")
public class DeviceCipherServiceImpl implements IDeviceCipherService {

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;

    @Autowired
    private HttpHelper httpHelper;

    @Resource
    private DeviceCipherDao deviceCipherDao;

    @Resource
    private DeviceCipherRuleDao deviceCipherRuleDao;

    @Resource
    private DeviceCipherApproveDao deviceCipherApproveDao;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysConstantDao sysConstantDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DeviceCipher queryById(Integer id) {
        return this.deviceCipherDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(DeviceCipherVO deviceCipher) {
        int pageNum = deviceCipher.getPageNum();
        int pageSize = deviceCipher.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        deviceCipher.setDeleteStatus(0);//未删除的
        List<DeviceCipher> result = deviceCipherDao.queryAll(deviceCipher);

        return ApiResult.success(new PageResult<>(page.getTotal(), result, deviceCipher.getPageSize(), deviceCipher.getPageNum()));

    }

    /**
     * 新增数据
     *
     * @param deviceCipher 实例对象
     * @return 实例对象
     */
    @Override
    public ApiResult insert(DeviceCipher deviceCipher, HttpServletRequest httpRequest) {
        if (StringUtils.isEmpty(deviceCipher.getIp())) {
            return ApiResult.error("ip不能为空");
        }
        if (!Pattern.compile(IP_REGEX).matcher(deviceCipher.getIp()).find()) {
            return ApiResult.error("ip不合法");
        }
        DeviceCipher queryDeviceCipher = deviceCipherDao.queryOneByIp(deviceCipher.getIp());
        DeviceCipherRule query = deviceCipherRuleDao.query();
        String desPassword = "";
        try {
            desPassword = AESUtil.desEncrypt(deviceCipher.getPassword()).trim();//先解密
        } catch (Exception e) {
            log.error("aes解密错误：{}", e);
        }
        deviceCipher.setPassword(deviceCipher.getPassword());
        deviceCipher.setAliveStatus(0);
        //TODO 密码有效性需要调用api验证
        deviceCipher.setIsValid(1);//验证接口未调通  目前默认账号有效
        deviceCipher.setIsStrong(Pattern.compile(query.getRuleChar().trim()).matcher(desPassword).find() ? 1 : 0);//符合密码规则即为强密码 否则为弱密码
        deviceCipher.setDeleteStatus(0);
//        deviceCipher.setLastStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//上一次重启时间 默认不给值
        deviceCipher.setUpdateUser(getUserSession(httpRequest).getUserName());
        deviceCipher.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        int result = 0;
        if (queryDeviceCipher == null) {
            deviceCipher.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            result = deviceCipherDao.insert(deviceCipher);
        } else {
            result = deviceCipherDao.updateByIp(deviceCipher);
        }
        if (result > 0) {
            return ApiResult.success();
        }
        return ApiResult.error("新增失败");
    }

    @Override
    public ApiResult delete(Integer id) {
        DeviceCipher deviceCipher = new DeviceCipher();
        deviceCipher.setId(id);
        deviceCipher.setDeleteStatus(1);
        deviceCipherDao.update(deviceCipher);
        return ApiResult.success();
    }

    /**
     * 修改数据
     *
     * @param deviceCipher 实例对象
     * @return 实例对象
     */
    @Override
    public ApiResult update(DeviceCipher deviceCipher) {
        DeviceCipherRule query = deviceCipherRuleDao.query();
        try {
            String pwd = AESUtil.desEncrypt(deviceCipher.getPassword()).trim();
            if (pwd.length() < 6 || pwd.length() > 10) {
                return ApiResult.error("密码长度不符合规范");
            }
            deviceCipher.setIsStrong(Pattern.compile(query.getRuleChar().trim()).matcher(pwd).find() ? PWD_STRONG_TRUE : PWD_STRONG_FALSE);//密码符合  字母大小写 数字 特殊字符即为强密码
        } catch (Exception e) {
            log.info("encrypt-error:{}", e);
            deviceCipher.setIsStrong(PWD_STRONG_FALSE);
        }
        //将原来申请 查看该设备密码 的用户的状态改为5
        DeviceCipherApproveVO deviceCipherApproveVO = new DeviceCipherApproveVO();
        deviceCipherApproveVO.setApproveStatus(2);
        deviceCipherApproveVO.setIp(deviceCipher.getIp());
        List<DeviceCipherApproveVO> deviceCipherApproveVOS = deviceCipherApproveDao.queryAll(deviceCipherApproveVO);
        for (DeviceCipherApproveVO cipherApproveVO : deviceCipherApproveVOS) {
            cipherApproveVO.setApproveStatus(5);//状态改为5
            deviceCipherApproveDao.updateStatus(cipherApproveVO);
        }
        int update = deviceCipherDao.update(deviceCipher);
        return 1 == update ? ApiResult.success() : ApiResult.error("修改失败");
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.deviceCipherDao.deleteById(id) > 0;
    }

    @Override
    public void template(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public ApiResult viewPwd(String ip, HttpServletRequest request) {
        UserSession userSession = getUserSession(request);
        if (!"admin".equalsIgnoreCase(userSession.getUserName())) {
            DeviceCipherApprove deviceCipherApprove = deviceCipherApproveDao.queryOne(new DeviceCipherApprove(ip, userSession.getUserName()));
            if (deviceCipherApprove == null) {
                return ApiResult.success().data(0).message("您暂无权限查看该设备密码，请先申请");//无权限 需要申请
            }
            if (4 == deviceCipherApprove.getApproveStatus()) {
                return ApiResult.success().data(0).message("您上次的申请已过期，请重新申请");//状态异常
            }
            if (5 == deviceCipherApprove.getApproveStatus()) {
                return ApiResult.success().data(0).message("该设备密码已被修改，请重新申请");//状态异常
            }
            if (1 == deviceCipherApprove.getApproveStatus()) {
                return ApiResult.success().data(1);//审核中
            }
            if (3 == deviceCipherApprove.getApproveStatus()) {
                return ApiResult.success().data(0).message("您的请求被驳回，请重新申请");//驳回
            }
        }
        DeviceCipher deviceCipher = new DeviceCipher();
        deviceCipher.setIp(ip);
        deviceCipher = deviceCipherDao.queryOneByIp(ip);
        return ApiResult.success(deviceCipher == null ? new DeviceCipher() : deviceCipher);
    }

    @Override
    public ApiResult applyPwd(Integer id, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DeviceCipher deviceCipher = deviceCipherDao.queryById(id);
        UserSession userSession = getUserSession(request);
        if (deviceCipher == null) {
            return ApiResult.error("设备不存在");
        }
        if (1 == userSession.getIsAdmin()) {
            return ApiResult.error("管理员无需申请");
        }
        int success = 0;
        DeviceCipherApprove deviceCipherApprove = new DeviceCipherApprove();
        deviceCipherApprove.setIp(deviceCipher.getIp());
        deviceCipherApprove.setProposUser(userSession.getUserName());
        deviceCipherApprove.setApproveStatus(1);
        deviceCipherApprove.setCreateTime(sdf.format(new Date()));
        deviceCipherApprove.setUpdateTime(sdf.format(new Date()));
        success = deviceCipherApproveDao.insert(deviceCipherApprove);
        return 1 == success ? ApiResult.success("申请已提交") : ApiResult.error("申请失败!");
    }

    @Override
    public ApiResult reboot(String ip) {
        SysConstant sysConstant = sysConstantDao.selectOne(new SysConstant("device_reboot"));
        DeviceCipher deviceCipher = deviceCipherDao.queryOneByIp(ip);
        String pwd = "";
        try {
            pwd = AESUtil.desEncrypt(deviceCipher.getPassword());
        } catch (Exception e) {
            log.error("aes解密错误:{}", e);
        }
        String url = String.format(sysConstant.getConstantValue(), ip);
        Map<String, Object> params = new HashMap<>();
        params.put("ip", ip);
        params.put("username", deviceCipher.getAccount());
        params.put("password", pwd);
        try {
            HttpClientUtil.get(url, params);
        } catch (Exception e) {
            log.error("请求发送异常:{}", e);
        }
        return ApiResult.success("正在重启中");
    }

    @Override
    public ApiResult approve(DeviceCipherApproveVO deviceCipherApproveVO, HttpServletRequest request) {
        DeviceCipherApprove deviceCipherApprove = new DeviceCipherApprove();
        deviceCipherApprove.setId(deviceCipherApproveVO.getId());
        deviceCipherApprove.setApproveUser(getUserSession(request).getUserName());
        deviceCipherApprove.setApproveStatus(deviceCipherApproveVO.getApproveStatus());
        deviceCipherApprove.setRemark(deviceCipherApproveVO.getRemark());
        deviceCipherApproveDao.update(deviceCipherApprove);
        return ApiResult.success();
    }

    @Override
    public ApiResult password() {
        SysConstant sysConstant = new SysConstant();
        sysConstant.setConstantKey("pwd_");
        List<SysConstant> sysConstants = sysConstantDao.queryPwd(sysConstant);
        Map<String, Object> data = new HashMap<>();
        for (SysConstant constant : sysConstants) {
            if ("pwd_type".equalsIgnoreCase(constant.getConstantKey())) {
                String[] split = constant.getConstantValue().split(",");
                int[] types = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
                data.put("pwd_type", types);
            }
            if ("pwd_length".equalsIgnoreCase(constant.getConstantKey())) {
                data.put("pwd_length", Integer.parseInt(constant.getConstantValue()));
            }
        }
        DeviceCipherRule query = deviceCipherRuleDao.query();
        data.put("pwd_period", query.getRulePeriod());
        return ApiResult.success(data);
    }

    public ApiResult readExcel(String filePath, String fileName, UserSession userSession) {
        File file = getRemoteFile(filePath);
        if (file == null) {
            return null;
        }
        String absoluteNewFilePath = this.uploadFolder + filePath;
        log.info("file#{}", file.getAbsolutePath());
        try {
            ExcelEntity linkResult = checkFile(fileName, file, absoluteNewFilePath, getAssetReader());
            if (!linkResult.getValid()) {
                log.error("文件校验失败#" + absoluteNewFilePath);
                return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
            }

            if (linkResult == null) {
                return ApiResult.error("未填写任何设备!");
            }
            List<DeviceCipherExcelImport> list = linkResult.getEntity().convertToRawTable(DeviceCipherExcelImport.class);
            if (list == null || list.size() == 0) {
                return ApiResult.error("未填写任何设备!!");
            }
            log.info("link.size#{}", list.size());
            return saveExcel(list, userSession);
        } catch (Exception ex) {
            log.error("读取设备上传文件失败#" + absoluteNewFilePath, ex);
            return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
        }
    }

    @Override
    public ApiResult importFile(MultipartFile file, HttpServletRequest request) {
        if (!ExcelHelper.checkExtension(file)) {
            return ApiResult.error(SERVER_UPLOAD_MIME_ERROR).appendMessage("不是Excel类型");
        }
        long size = file.getSize();
        if (size > UPLOAD_FILE_MAX_SIZE) {
            return ApiResult.error(SERVER_UPLOAD_MAX_SIZE_ERROR)
                    .appendMessage("最大" + FileHelper.getSizeInMb(UPLOAD_FILE_MAX_SIZE) + "MB");
        }

        // 上传文件，持久化到本地，写数据库
        String originName = file.getOriginalFilename();
        String newFileName = fileService.getNewFileName(originName);
        boolean saved = FileHelper.saveFile(file, uploadFolder, newFileName);
        if (saved) {
            UserSession userSession = getUserSession(request);
            return readExcel(newFileName, originName, userSession);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult dashboard() {
        Map<String, Integer> data = new HashMap<>();
        DeviceCipherVO deviceCipherVO = new DeviceCipherVO();
        deviceCipherVO.setDeleteStatus(0);
        List<DeviceCipher> deviceCiphers = deviceCipherDao.queryAll(deviceCipherVO);
        data.put("deviceSize", deviceCiphers.size());
        int i = sysUserMapper.selectCount(new SysUser());
        data.put("userSize", i);
        DeviceCipherApproveVO deviceCipherApprove = new DeviceCipherApproveVO();
        deviceCipherApprove.setApproveStatus(1);
        List<DeviceCipherApproveVO> deviceCipherApproveVOS = deviceCipherApproveDao.queryAll(deviceCipherApprove);
        data.put("approvalPending", deviceCipherApproveVOS.size());
        return ApiResult.success(data);
    }

    @Override
    public ApiResult randomPwd() {
        DeviceCipherRule query = deviceCipherRuleDao.query();
        SysConstant sysConstant = sysConstantDao.selectOne(new SysConstant("pwd_type"));
        String[] split = sysConstant.getConstantValue().split(",");
        List<Integer> data = new ArrayList<>();
        for (String s : split) {//将对应字符的
            switch (Integer.parseInt(s)) {
                case 1:
                    for (int i = 65; i <= 90; i++) {
                        data.add(i);
                    }
                    break;
                case 2:
                    for (int i = 97; i <= 122; i++) {
                        data.add(i);
                    }
                    break;
                case 3:
                    for (int i = 48; i < 57; i++) {
                        data.add(i);
                    }
                    break;
                case 4:
                    Collections.addAll(data, 33, 35, 36, 37, 38, 42, 64, 94, 95);//!@#$%^&*_
                    break;
            }
        }
        Collections.shuffle(data);
        int size = data.size();
        int[] ints = data.stream().mapToInt(x -> x).toArray();
        StringBuilder newPwd = new StringBuilder();
        //根据规则生成密码
        while (!Pattern.compile(query.getRuleChar().trim()).matcher(newPwd.toString()).find()) {
            newPwd = new StringBuilder();
            for (Integer i = 0; i < query.getRuleLength(); i++) {
                int randomNum = new Random().nextInt(size);
                int num = ints[randomNum];
                newPwd.append((char) num);
            }
        }
        return ApiResult.success(newPwd.toString());
    }


    @Override
    public ApiResult deviceBrand() {
        Map<String, Object> data = new HashMap<>();
        List<SelectVO> list = new ArrayList<>();
        Collections.addAll(list, new SelectVO(1, "大华"), new SelectVO(2, "宇视"), new SelectVO(3, "海康"));
        data.put("brand", list);
        return ApiResult.success(data);
    }

    protected ApiResult saveExcel(List<DeviceCipherExcelImport> list, UserSession userSession) {
        List<DeviceCipher> data = new ArrayList<>();
        String userName = userSession.getUserName();
        SysConstant sysConstant = new SysConstant();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DeviceCipherRule query = deviceCipherRuleDao.query();
        sysConstant.setConstantKey("device_check");
        sysConstant = sysConstantDao.selectOne(sysConstant);
        Integer index = 0;
        for (DeviceCipherExcelImport deviceCipherExcel : list) {
            if (!Pattern.compile(IP_REGEX).matcher(deviceCipherExcel.getIp()).find() || StringUtils.isEmpty(deviceCipherExcel.getAccount()) || StringUtils.isEmpty(deviceCipherExcel.getPassword()) || StringUtils.isEmpty(deviceCipherExcel.getDeviceBrand())){
                return ApiResult.error("请按照正确格式填写带*内容");
            }
            DeviceCipher deviceCipher = new DeviceCipher();
            BeanUtils.copyProperties(deviceCipherExcel, deviceCipher);
            //TODO 是否在线
            deviceCipher.setAliveStatus(1);//导入默认在线
            boolean matches = Pattern.compile(query.getRuleChar().trim()).matcher(deviceCipherExcel.getPassword()).find();
//            String urlBrand = "大华".equals(deviceCipherExcel.getDeviceBrand()) ? "Dahua" : ("海康".equals(deviceCipherExcel.getDeviceBrand()) ? "hik" : "ys");
//            String url = String.format(sysConstant.getConstantValue(), urlBrand);
//            Map<String, Object> params = new HashMap<>();
//            params.put("ip", deviceCipherExcel.getIp());
//            params.put("username", deviceCipherExcel.getAccount());
//            params.put("password", deviceCipherExcel.getPassword());
//            String response = "";
//            Integer pwdIsValid = 0;
//            try {
//                response = HttpClientUtil.get(url, params);
//            } catch (Exception e) {
//                log.info("http-get-error:{}", e);
//                response = JSONObject.toJSONString(ApiResult.error("请求失败"));
//            }
//            ApiResult apiResult = JSONObject.parseObject(response, ApiResult.class);
//            if (0 == apiResult.getCode()) {
//                pwdIsValid = 1;
//            }
            try {
                deviceCipher.setPassword(AESUtil.encrypt(deviceCipherExcel.getPassword()));//存储密文
            } catch (Exception e) {
                e.printStackTrace();
            }
            deviceCipher.setIsValid(1);//目前默认有效
            deviceCipher.setIsStrong(matches ? PWD_STRONG_TRUE : PWD_STRONG_FALSE);
            deviceCipher.setDeleteStatus(0);//默认未删除
            deviceCipher.setUpdateUser(userName);
//            deviceCipher.setLastStartTime(sdf.format(new Date()));
            deviceCipher.setCreateTime(sdf.format(new Date()));
            deviceCipher.setUpdateTime(sdf.format(new Date()));
            data.add(deviceCipher);
        }
        if (!CollectionUtils.isEmpty(data)) {
            deviceCipherDao.insertOrUpdateBatch(data);
        }
        return ApiResult.success();
    }

    private ExcelSheetReader getAssetReader() {
        return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(5)).sheetNo(0)
                .notNullFields(Lists.newArrayList("ip", "account", "password", "deviceBrand"))
                .headerClass(DeviceCipherExcelImport.class).build();
    }

    private ExcelEntity checkFile(String fileName, File file, String outFilePath, ExcelSheetReader reader) {
        ExcelEntity result = new ExcelEntity();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, true);
            Sheet sheet = ExcelHelper.getSheet(workbook, reader);
            SheetEntity sheetEntity = ExcelHelper.readExcelSheet(sheet, reader);
            is.close();
            is = null;
            sheetEntity.resetRuleMap();

            checkInput(sheet, sheetEntity, true, dataStyle);
            result.setValid(sheetEntity.getData().size() != 0 ? sheetEntity.getValid() : false);
            result.setEntity(sheetEntity);

            // 校验不通过，修改文件
            if (!result.getValid()) {
                os = new FileOutputStream(new File(outFilePath));
                workbook.write(os);
            }
        } catch (Exception e) {
            log.error("检查文件错误error:{}", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception ex1) {
                log.error("readFile error, close error", ex1);
            }
        }
        return result;
    }

    /**
     * 校验 excel输入，直接保存校验结果到 poi 对象
     *
     * @param poiSheet    poi 的sheet
     * @param sheetEntity 数据对象
     * @param writeExcel  true，校验结果写入excel； false，不写文件
     */
    private void checkInput(Sheet poiSheet, SheetEntity sheetEntity, boolean writeExcel, CellStyle cellStyle) {
        boolean validResult = true;
        for (ExcelRow excelRow : sheetEntity.getData()) {
            int rowIndex = excelRow.getRow();

            Row poiRow = poiSheet.getRow(rowIndex);
            log.info("row[{}], {}", rowIndex, (poiRow != null));
            for (String name : excelRow.getData().keySet()) {
                ExcelColumn column = excelRow.getColumn(name);
                int colIndex = column.getCol();
                Object val = column.getValue();
                String[] rules = sheetEntity.getValidateRule(name);
                // 数据校验
                ValidateResult result = ValidateHelper.doValidate(val, rules);
                column.setValid(result.getPassed());
                if (result.getPassed()) {
                    continue;
                }
                validResult = false;
                String message = result.getMessages();

                // 更新内存对象的值
                String strVal = (val == null) ? "" : val.toString();
                if (!strVal.contains(message)) {
                    strVal = strVal + message;
                    column.setValue(strVal);
                }
                log.error("validate#{}-{}, {}, {}, {}", rowIndex, colIndex, name, message, strVal);

                log.info("col[{}]#{}, error#{}", colIndex, name, strVal);
                if (!writeExcel) {
                    continue;
                }
                // 更新Excel的值
                Cell poiCell = poiRow.getCell(colIndex);

                if (poiCell == null) {
                    poiCell = poiRow.createCell(colIndex);
                    poiCell.setCellStyle(cellStyle);
                }
                poiCell.setCellValue(strVal);
            }
        }
        sheetEntity.setValid(validResult);
    }

    private File getRemoteFile(String filePath) {
        if (filePath == null) {
            return null;
        }
        File file = new File(this.uploadFolder + "/" + filePath);
        if (!file.exists()) {
            log.error("file error#" + this.uploadFolder + "/" + filePath);
            return null;
        }
        return file;
    }

    private static boolean checkIsValid(String deviceName, Map<String, Object> data) throws Exception {
        String device = "";
        switch (deviceName) {
            case "大华":
                device = "Dahua";
                break;
            case "海康":
                device = "hik";
                break;
            case "宇视":
                device = "ys";
                break;
        }
        data.put("ip", "192.168.2.9");
        data.put("username", "admin");
        data.put("password", "Admin123");
        String format = String.format("http://192.168.9.196:28700/api/%s/login", device);
        String s = HttpClientUtil.get(format, data);
        System.out.println(s);
        return false;
    }

    public UserSession getUserSession(HttpServletRequest request) {
        return this.httpHelper.getUser(request);
    }
}
