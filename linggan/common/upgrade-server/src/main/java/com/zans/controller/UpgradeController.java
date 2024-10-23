package com.zans.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.config.Constants;
import com.zans.dao.DbVersionDao;
import com.zans.util.CompressUtil;
import com.zans.util.FileHelper;
import com.zans.util.MyTools;
import com.zans.vo.AnsibleVO;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.zans.config.Constants.ANSIBLE_PLAYBOOK;
import static com.zans.config.Constants.HOME;

/**
 * @author beixing
 * @Title: upgrade-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/2/24
 */
@Controller
@Slf4j
public class UpgradeController {

    @Resource
    private DbVersionDao dbVersionDao;

    @Value("${uploadFolder}")
    private String  uploadFolder;


    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/")
    public String uploadPage(){
        return "upload";
    }


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request, Model model)  {
        // 上传文件，持久化到本地
        long startTime = System.currentTimeMillis();
        String originName = file.getOriginalFilename();
        if (!originName.matches(".*\\.zip") ) {
            model.addAttribute("data","请上传zip压缩包文件！");
            return "uploadFail";
        }
        JSONObject result = new JSONObject();
        FileHelper.saveFile(file, uploadFolder, originName);
        handle(originName, uploadFolder, Constants.ZIP_PWD,result);
        long endTime = System.currentTimeMillis();
        log.info("完成此次升级,总耗时:{}",(endTime-startTime)+"ms");
        model.addAttribute("data",result.toJSONString());
        return "uploadOk";
    }


    private  void handle(String originName, String uploadFolder, String pwd, JSONObject result) {
        String source = uploadFolder + File.separator + originName;
        long startTime = System.currentTimeMillis();
        log.info("开始进行解压...");
        uploadFolder += File.separator+ MyTools.getNowTime("yyyyMMdd");
        Map<String, String> map = null;
        try {
            map = CompressUtil.unZip(source, uploadFolder, pwd);
        } catch (ZipException e) {
            log.error("压缩包解压失败！原因:",e);
            result.put("error","压缩包解压失败!请检查压缩包是否损坏或密码是否正确!");
            return;
        }
        long endTime = System.currentTimeMillis();
        log.info("完成解压,耗时:{},文件路径:{},",(endTime-startTime)+"ms",map);
        result.put("unpackTime",(endTime-startTime));

        String upgradePath = map.get("upgrade.json");
        JSONObject jsonObject = (JSONObject) FileHelper.readFileToJson(upgradePath);
        //进行检查
        checkListBefore(map,jsonObject,result);
        if(!StringUtils.isEmpty(result.getString("error"))){
            return;
        }
        //升级脚本
//        upgradeScript(map, jsonObject,result);

        //升级程序
        upgradeProgram(map,source, jsonObject,result);

        checkListAfter(map,jsonObject,result);

    }

    //检查压缩包清单是否符合
    private void checkListBefore(Map<String, String> map, JSONObject jsonObject, JSONObject result) {
        JSONObject before = jsonObject.getJSONObject("checkList").getJSONObject("before");
        Integer fileNum = before.getInteger("fileNum");
        if(fileNum!=null && fileNum == map.size()){
            result.put("checkListBefore","解压文件检测成功!");
            return;
        }
        result.put("error","解压文件检测不符!请检查压缩包!");
    }

    //检查升级后是否符合
    private void checkListAfter(Map<String, String> map, JSONObject jsonObject, JSONObject result) {
        JSONObject after = jsonObject.getJSONObject("checkList").getJSONObject("after");

    }

    private void upgradeScript(Map<String, String> map, JSONObject jsonObject, JSONObject result) {
        JSONObject dbJson = jsonObject.getJSONObject("upgrade").getJSONObject("db");
        String before = dbJson.getJSONObject("before").getString("cmd");
        if(!StringUtils.isEmpty(before)){
            execute(before,10);
        }
        String after = dbJson.getJSONObject("after").getString("cmd");
        if(!StringUtils.isEmpty(after)){
            execute(after,10);
        }
        JSONArray dbArray = dbJson.getJSONArray("execute");
        for (Object o : dbArray) {
            JSONObject jsonObject1 = (JSONObject) o;
            String name = jsonObject1.getString("fullName");
            String sqlPath = map.get(name);
            String sql = FileHelper.readFileToString(sqlPath);
            log.info("sql语句:{}",sql);
            dbVersionDao.executeSql(sql);
            log.info("执行完成!");
        }
    }

    private void upgradeProgram(Map<String, String> map,String source, JSONObject jsonObject, JSONObject result) {
        long startTime;
        long endTime;
        JSONArray programArray = jsonObject.getJSONObject("upgrade").getJSONArray("program");
        for (Object o : programArray) {
            JSONObject jsonObject1 = (JSONObject) o;
            Integer enable = jsonObject1.getInteger("enable");
            if(enable == null || enable == 0){
                continue;
            }
            String name = jsonObject1.getString("name");
            String srcPath = map.get(name.concat(".jar"));
            String dstPath = String.format(HOME,name);
            startTime = System.currentTimeMillis();
            JSONArray glb = new JSONArray();
            AnsibleVO ansibleVO = new AnsibleVO();
            ansibleVO.setDstPath(dstPath);
            ansibleVO.setSrcPath(srcPath);
            ansibleVO.setSuperName(name.replace("-","_"));
            glb.add(ansibleVO);
            JSONObject glbJSON = new JSONObject();
            glbJSON.put("glb",glb);
            String cmd = String.format(ANSIBLE_PLAYBOOK,glbJSON.toString());
            log.info("开始升级:{}的项目!执行命令为:{}",name,cmd);
            //1.执行ansible的命令
            execute(cmd,10);
            endTime = System.currentTimeMillis();
            log.info("完成:{}项目的升级!耗时:{}!",name,(endTime-startTime)+"ms");
        }
    }


    private void execute(String command,int time)  {
        Process process = null;
        BufferedReader br = null;
        try {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) {
                process = Runtime.getRuntime().exec(command);
            } else {
                process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            }

//            StringBuffer sbf = new StringBuffer();
//            String line = null;
//            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            long now = System.currentTimeMillis();
//            while ((line = br.readLine()) != null) {
//                sbf.append(line);
//                sbf.append(" ");
//                if ((System.currentTimeMillis() - now) > time * 1000) {
//                    process.destroy();
//                }
//            }
//            String resultInfo = sbf.toString();
//            log.info("result:{}", resultInfo);

        }catch (IOException e){
            log.error("执行命令失败！执行的命令:{},原因:",command,e);
        } finally {
            if(process.isAlive()){
                process.destroy();
            }
//            try {
//                br.close();
//            } catch (IOException e) {
//                log.error("关闭流失败!");
//            }
        }

    }


    public static void main(String[] args) throws ZipException {
        String originName = "升级包.zip";
        String uploadFolder = "D:";
        String pwd = "$ZSfdg78963&*!";
        JSONObject result = new JSONObject();
        UpgradeController controller = new UpgradeController();
        controller.handle(originName, uploadFolder, pwd, result);
    }
}
