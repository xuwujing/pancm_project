package com.zans.mms.controller.pc;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.zans.base.util.DateHelper;
import com.zans.base.util.FileHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.mms.service.IDbVersionService;
import com.zans.mms.util.GetProperties;
import com.zans.mms.vo.DbVersionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.sun.jmx.snmp.EnumRowStatus.active;
import static com.zans.base.config.GlobalConstants.VERSION_NAME;

/**
 * @Author pancm
 * @Description
 * @Date  2021/1/6
 * @Param
 * @return
 **/
@Api(value = "/hello", tags = {"/hello ~ 欢迎页，测试用户登陆状态"})
@RestController
@RequestMapping("/")
@Slf4j
public class WelcomeController {

    @Value("${spring.profiles.active}")
    String active;

    @Autowired
    private IDbVersionService dbVersionService;

    @ApiOperation(value = "/hello", notes = "无参数，返回 hello world | 当前时间 | 用户登陆信息")
    @RequestMapping(value = "/hello", method = {RequestMethod.GET})
    public ApiResult sayHello(){
        String now = DateHelper.getNow();
        Map<String, Object> result = MapBuilder.getBuilder().put("hello", "world")
                .put("now", now)
                .put("version", FileHelper.readResourcesFile(VERSION_NAME))
                .build();
        return ApiResult.success(result);
    }


    @ApiOperation(value = "/down", notes = "文件下载")
    @RequestMapping(value = "/down", method = {RequestMethod.GET})
    public void down() throws Exception {
        createAllPdf();
    }

    @ApiOperation(value = "/version", notes = "版本信息")
    @RequestMapping(value = "/version", method = {RequestMethod.GET})
    public JSONObject version(HttpServletRequest request){
        Map<String, String> map = GetProperties.getAppSettings();
        JSONObject result = new JSONObject();
        result.put("git_branch", map.get("git_branch"));
        result.put("build_time", map.get("build_time"));
        result.put("git_commit", map.get("git_commit"));
        result.put("app_name", map.get("build_app"));
        result.put("profile", active);
        result.put("version", FileHelper.readResourcesFile(VERSION_NAME));
        DbVersionVO dbVersionVO =  dbVersionService.queryNewOne();
        if(dbVersionVO!=null){
            result.put("db_version", dbVersionVO.getVersion());
            result.put("db_remark", dbVersionVO.getRemark());
            result.put("db_name", "mms_dev11");
        }
        return result;
    }


    public void createAllPdf() throws Exception {
        //填充创建pdf
        PdfReader reader = null;
        PdfStamper stamp = null;
        try {
            reader = new PdfReader("E:/module.pdf");
            SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
            String times = simp.format(new Date()).trim();
            //创建生成报告名称
            String root = "E:/pdf" + File.separator;
            if (!new File(root).exists()) {
                new File(root).mkdirs();
            }
            File deskFile = new File(root, times + ".pdf");
            stamp = new PdfStamper(reader, new FileOutputStream(deskFile));
            //取出报表模板中的所有字段
            AcroFields form = stamp.getAcroFields();
            // 填充数据
            form.setField("name", "zhangsan");
            form.setField("sex", "男");
            form.setField("age", "15");

            //报告生成日期
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            String generationdate = dateformat.format(new Date());
            form.setField("generationdate", generationdate);
            stamp.setFormFlattening(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stamp != null) {
                stamp.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }
}
