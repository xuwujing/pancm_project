package com.zans.mms.controller.demokit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zans.base.controller.BaseController;
import com.zans.base.util.HttpClientUtil;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.service.IAssetService;
import com.zans.mms.service.IBaseDeviceTypeService;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.IVideoDiagnosisService;
import com.zans.mms.vo.asset.AssetQueryVO;
import com.zans.mms.vo.asset.AssetViewResVO;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosticThresholdReqVO;
import com.zans.mms.vo.asset.diagnosis.AssetThresholdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @version V1.0
 **/
@RestController
@RequestMapping("demoKit/diagnosis")
@Api(tags = "视频诊断")
@Validated
@Slf4j
public class VideoDiagnosisController extends BaseController {

    @Autowired
    IConstantItemService constantItemService;
    @Autowired
    private IAssetService assetService;

    @Autowired
    IBaseDeviceTypeService baseDeviceTypeService;

    @Autowired
    private IVideoDiagnosisService videoDiagnosisService;

    @Value("${request.diagnostic.url}")
    private String diagnosticUrl;

    @Value("${request.diagnostic.imgUrl}")
    private String diagnosticImgUrl;

    @Value("${request.ocr.url}")
    private String ocrUrl;

    // 视频诊断故障类型
    public static String VIDEO_FAULT_TYPE = "faultType";

    // 视频诊断故障类型结果
    public static String VIDEO_FAULT_TYPE_RESULT = "faultTypeResult";

    // 视频诊断故障整体结果
    public static String VIDEO_DIAGNOSIS_RESULT = "diagnosisResult";




    @ApiOperation(value = "/monitor/init", notes = "")
    @RequestMapping(value = "/monitor/init", method = {RequestMethod.GET})
    public ApiResult init(){
        List<SelectVO> videoFaultTypeList = constantItemService.findItemsByDict(VIDEO_FAULT_TYPE);
        List<SelectVO> videoFaultTypeResultLList = constantItemService.findItemsByDict(VIDEO_FAULT_TYPE_RESULT);
        List<SelectVO> videoDiagnosisResultLList = constantItemService.findItemsByDict(VIDEO_DIAGNOSIS_RESULT);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("video_fault_type",videoFaultTypeList)
                .put("video_fault_type_result",videoFaultTypeResultLList)
                .put("video_diagnosis_result",videoDiagnosisResultLList)
                .build();
        return ApiResult.success(result);
    }


    /**
     *  获取所有数据
     *
     * @param vo  AssetReqVo
     * @return     ApiResult<PageInfo<AssetResVo>>
     */
    @ApiOperation(value = "获取数据列表", notes = "获取数据列表")
    @PostMapping(value = "/monitor/list",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getMonitorList(@Valid @RequestBody AssetQueryVO vo) {
        super.checkPageParams(vo);
        return assetService.getMonitorList(vo);
    }


    /**
     *  获取监控视频诊断的信息
     *
     * @param ip    ip
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "查询视频诊断数据", notes = "查询视频诊指定数据")
    @GetMapping(value = "/monitor/diagnosis/view",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<AssetViewResVO> monitorDiagnosisView(@RequestParam(value = "ip") String ip, @RequestParam(value = "traceId",defaultValue = "") String traceId) {
        return assetService.getDiagnosisView(ip,traceId);
    }


    /**
     *  获取监控视频诊断的信息
     *
     * @param
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "进行视频诊断", notes = "进行视频诊断")
    @GetMapping(value = "/monitor/diagnosis/video",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<AssetViewResVO> diagnosisVideo(@RequestParam(value = "assetCode",defaultValue = "")  String assetCode,
                                                    @RequestParam(value = "ipAddress")  String ipAddress) {
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("deviceId",assetCode);
        jsonObject.put("ipAddress",ipAddress);
        try {
            String data =  HttpClientUtil.get(diagnosticUrl, jsonObject);
            if(!"suc".equals(data)){
                return ApiResult.error("请求诊断算法失败!原因:"+data);
            }
        } catch (Exception e) {
            log.error("请求诊断算法失败");
            return ApiResult.error("请求诊断算法失败!");
        }
        log.info("请求视频诊断接口成功！请求参数:{}",jsonObject);
        return ApiResult.success();
    }


    /**
     *  获取诊断图片中的文字
     *
     * @param
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "获取诊断图片中的文字", notes = "获取诊断图片中的文字")
    @GetMapping(value = "/monitor/diagnosis/ocr",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult diagnosisOcr(@RequestParam(value = "imgUrl") String imgUrl) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imgUrl",diagnosticImgUrl+imgUrl);
        try {
            String result = HttpClientUtil.get(ocrUrl,jsonObject);
            ApiResult data = JSON.parseObject(result,  ApiResult.class);
            log.info("请求图片文字识别接口成功！返回参数:{}",data);
            if(data.getCode()!=0){
                return ApiResult.error("提取图片文字失败！请检查图片是否合法!");
            }
            String msg = ((JSONObject)data.getData()).getJSONArray("value").get(0).toString();
            List<String> list = JSONObject.parseArray(msg, String.class);
            return ApiResult.success(list);
        } catch (Exception e) {
            log.error("请求图片文字识别失败",e);
            return ApiResult.error("请求图片文字识别失败!");
        }
    }




    private ApiResult test1(JSONObject jsonObject, String imgPath) throws Exception {
        String url = diagnosticImgUrl + imgPath;
        String img = HttpClientUtil.get(url);
        List<String> list = new ArrayList<>();
        list.add("image");
        List<String> list2 = new ArrayList<>();
//            list2.add(getByte(url));
        jsonObject.put("key",list);
//            String v = base64DeStr(getByte(url));
        byte[] s = getImageBinary("D:\\222.jpg");
//            String v = base64DeStr(s);
        String v = new String(new org.apache.commons.codec.binary.Base64().decode(s),"utf-8");
        list2.add(v);
        jsonObject.put("value",list2);
//            log.info("请求ocr参数:{}",jsonObject);
        String data =   HttpClientUtil.post3(ocrUrl,jsonObject.toJSONString());
//            log.info("请求ocr返回结果:{}",data);
        if(!"suc".equals(data)){
            return ApiResult.error("请求诊断算法失败!原因:"+data);
        }
        return null;
    }


    static byte[] getImageBinary(String path){
        byte[] imageBytes = null;
        try (FileInputStream fileInputStream = new FileInputStream(new File(path))) {
            imageBytes = new byte[fileInputStream.available()];
            fileInputStream.read(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(imageBytes);
        return imageBytes;
    }

    public static String base64DeStr(byte[] encodeStr) throws UnsupportedEncodingException {

//        byte[] decodeStr = Base64.getDecoder().decode(new String(encodeStr).replace("\r\n", ""));
        byte[] decodeStr = Base64.getDecoder().decode(encodeStr);
        return new String(decodeStr,  "UTF-8");
    }

    public static byte[] getByte(String imgUrl) {

        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            // connection.getResponseCode();
            connection.setConnectTimeout(10000);
            BufferedImage image = ImageIO.read(connection.getInputStream());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", out);

            return out.toByteArray();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }


    public static void main(String[] args) {

    }


    /**
     *  设备阈值配置
     *
     * @param
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "设备阈值配置", notes = "设备阈值配置")
    @PostMapping(value = "/monitor/diagnosis/config",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult diagnosisConfig(@RequestBody AssetDiagnosticThresholdReqVO assetDiagnosticThresholdReqVOS) {
        return videoDiagnosisService.diagnosisConfig(assetDiagnosticThresholdReqVOS);
    }

    /**
     *  设备阈值配置
     *
     * @param
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "设备阈值配置", notes = "设备阈值配置")
    @GetMapping(value = "/monitor/diagnosis/config/view",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult diagnosisConfigView(@RequestParam(value = "deviceId",defaultValue = "")  String deviceId,
                                         @RequestParam(value = "ipAddress")  String ipAddress) {
        return videoDiagnosisService.diagnosisConfigView(deviceId,ipAddress);
    }
    /**
     *  设备全局阈值配置
     *
     * @param
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "设备全局阈值配置", notes = "设备全局阈值配置")
    @PostMapping(value = "/monitor/diagnosis/overall/config",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult diagnosisOverallConfig(@RequestBody List<AssetThresholdVO> assetThresholdVOS) {
        return videoDiagnosisService.diagnosisOverallConfig(assetThresholdVOS);
    }

    /**
     *  设备全局阈值配置查询
     *
     * @param
     * @return     ApiResult<AssetResVo>
     */
    @ApiOperation(value = "设备全局阈值配置查询", notes = "设备全局阈值配置查询")
    @GetMapping(value = "/monitor/diagnosis/overall/view",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult diagnosisOverallConfig() {
        return videoDiagnosisService.diagnosisOverallView();
    }

}
