package com.zans.mms.controller.demokit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zans.base.controller.BaseController;
import com.zans.base.util.HttpClientUtil;
import com.zans.base.util.HttpHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.guard.AssetMapper;
import com.zans.mms.service.IAssetService;
import com.zans.mms.service.IBaseDeviceTypeService;
import com.zans.mms.service.IFileService;
import com.zans.mms.util.Base64Util;
import com.zans.mms.vo.asset.AssetMonitorResVO;
import com.zans.mms.vo.asset.AssetQueryVO;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoRespVO;
import com.zans.mms.vo.common.ApiRequestVO;
import com.zans.mms.vo.qrcode.QrCodeReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.zans.base.config.GlobalConstants.MONITOR;


/**
 * @author beixing
 * @Title: QrCodeController
 * @Description: 二维码
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2021/5/20
 **/
@RestController
@RequestMapping("demoKit/qrcode")
@Api(tags = "单项传输")
@Slf4j
public class QrCodeController extends BaseController {


    @Autowired
    private IAssetService assetService;

    @Autowired
    IBaseDeviceTypeService baseDeviceTypeService;
    @Autowired
    private HttpHelper httpHelper;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;


    @Value("${request.qrcode.url}")
    private String qrcodeUrl;

    @Value("${request.mms.url}")
    private String mmsUrl;

    @Value("${mms.request.enable:true}")
    private boolean mmsEnable;

    @Autowired
    private AssetMapper assetMapper;


    private static Map<String, JSONObject> ipMap = new ConcurrentHashMap<>();

    private static String traceId = null;

    /**
     * 获取所有数据
     *
     * @param vo AssetReqVo
     * @return ApiResult<PageInfo < AssetResVo>>
     */
    @ApiOperation(value = "获取单项传输列表", notes = "获取单项传输列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getQrCodeList(@Valid @RequestBody AssetQueryVO vo) {
        super.checkPageParams(vo);
        String deviceType = baseDeviceTypeService.getTypeIdByName(MONITOR);
        vo.setDeviceType(deviceType);
        return assetService.getMonitorList(vo);
    }

    /**
     * 获取信息
     *
     * @param
     * @return
     */
    @ApiOperation(value = "生成二维码数据", notes = "生成二维码数据")
    @PostMapping(value = "/produce", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult monitorView(@RequestBody QrCodeReqVO qrCodeReqVO) {
        super.checkPageParams(qrCodeReqVO);
        AssetQueryVO assetQueryVO = new AssetQueryVO();
        BeanUtils.copyProperties(qrCodeReqVO, assetQueryVO);
        ApiResult apiResult = assetService.getMonitorList(assetQueryVO);
        List<AssetMonitorResVO> result = ((PageResult) apiResult.getData()).getList();
        List<AssetDiagnosisInfoRespVO> assetDiagnosisInfoRespVOS = new ArrayList<>();
        for (AssetMonitorResVO assetMonitorResVO : result) {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            jsonObject = jsonObject.parseObject(JSONObject.toJSONString(assetMonitorResVO));
            jsonObject2 = jsonObject.parseObject(JSONObject.toJSONString(qrCodeReqVO));
            jsonObject.putAll(jsonObject2);
//            jsonObject.remove("faultTypesName");
//            jsonObject.remove("deviceType");
            jsonObject.remove("page_num");
            jsonObject.remove("page_size");
            jsonObject.remove("diagnosisTime");
            jsonObject.remove("assetCode");
            jsonObject.remove("projectName");
            jsonObject.remove("deviceTypeName");
            jsonObject.remove("faultTypesName");
            ApiResult apiResult1 = getQrCode(jsonObject);
            JSONObject resultData = (JSONObject) apiResult1.getData();
            String imgUrl = resultData.getString("imgUrl");
            AssetDiagnosisInfoRespVO assetDiagnosisInfoRespVO = assetMapper.getDiagnosisView(assetMonitorResVO.getIpAddr());
            assetDiagnosisInfoRespVO.setQrCodeImgUrl(imgUrl);
            assetDiagnosisInfoRespVOS.add(assetDiagnosisInfoRespVO);
            //调用工单生成方法
            createTickets(jsonObject);
        }
        return ApiResult.success(assetDiagnosisInfoRespVOS);
    }

    private void createTickets(JSONObject jsonObject) {
        if (!mmsEnable) {
            return;
        }
        try {
            String ip = getIp();
            String lng = null;
            String lat = null;
            if (ipMap.get(ip) != null) {
                lng = ipMap.get(ip).getString("longitude");
                lat = ipMap.get(ip).getString("latitude");
            } else {
                //获取经纬度
                JSONObject jsonObject1 = getGeocodeGaoDeGetLngLatIp(ip);
                lng = (jsonObject1.getString("location")).split(",")[0];
                lat = (jsonObject1.getString("location")).split(",")[1];
                JSONObject json = new JSONObject();
                json.put("longitude", lng);
                json.put("latitude", lat);
                ipMap.put(ip, json);
            }
            String newTraceId = jsonObject.getString("traceId");
            if (traceId != null) {
                if (traceId.equals(newTraceId)) {
                    log.info("not new data！");
                    return;
                }
            }
            traceId = newTraceId;

            jsonObject.put("longitude", lng);
            jsonObject.put("latitude", lat);
            log.info("请求运维系统请求地址:{},请求参数:{}!", mmsUrl, jsonObject);
            String result = HttpClientUtil.post2(mmsUrl, jsonObject.toJSONString());
            log.info("请求运维系统返回结果！result:{}", result);
        } catch (Exception e) {
            log.error("请求运维系统接口失败！请求参数:{}", jsonObject, e);
        }


    }


    private ApiResult getQrCode(JSONObject jsonObject) {
        ApiRequestVO apiRequestVO = new ApiRequestVO();
        apiRequestVO.setGroupId(1);
        try {
            String encode = Base64Util.base64EnStr(jsonObject.toJSONString());
            System.out.println(encode);
            apiRequestVO.setData(encode);
            String data = HttpClientUtil.post2(qrcodeUrl, JSONObject.toJSONString(apiRequestVO));
            ApiResult apiResult = JSONObject.parseObject(data, ApiResult.class);
            return apiResult;
        } catch (Exception e) {
            log.error("请求二维码生成项目失败!请求参数:{}", jsonObject, e);
        }
        return ApiResult.error("生成二维码失败!");
    }


    private String getIp() {
        String getIpUrl = "http://ip.dhcp.cn/?json";
        try {
            String data = HttpClientUtil.get(getIpUrl);
            JSONObject jsonObject = JSON.parseObject(data);
            log.info("当前的IP信息:{}", jsonObject);
            return jsonObject.getString("IP");
        } catch (Exception e) {
            log.error("请求失败！url:{}", getIpUrl, e);
        }
        return null;
    }

    /**
     * @return
     * @Author beixing
     * @Description 根据ip获取高德的经纬度
     * @Date 2021/8/5
     * @Param
     **/
    private JSONObject getGeocodeGaoDeGetLngLatIp(String ip) {
        JSONObject jsonObject = new JSONObject();
        JSONObject par = new JSONObject();
        par.put("key", "44cfa7574786e7f35a7f58b545f57819");
        par.put("type", 4);
        par.put("ip", ip);
        String getIpUrl = "https://restapi.amap.com/v5/ip";
        try {
            String data = HttpClientUtil.get(getIpUrl, par);
            jsonObject = JSON.parseObject(data);
            log.info("获取高德的信息:{}", jsonObject);
            return jsonObject;
        } catch (Exception e) {
            log.error("请求失败！url:{}", getIpUrl, e);
        }
        return jsonObject;
    }

}
