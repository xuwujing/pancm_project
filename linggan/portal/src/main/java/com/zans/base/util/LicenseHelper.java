package com.zans.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.LicenseVO;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class LicenseHelper {

    /***
     * 获取license的值
     * @param elementVO
     * @return
     * @throws Exception
     */
    public static String genLicense(LicenseVO elementVO) throws Exception {
        String publicKey = elementVO.getPublicKey();
        String sign = "EXPIREDAY=" + elementVO.getExpireday() + "MACADRESS" + elementVO.getMacadress() + "PRODUCTTYPE" + elementVO.getProducttype()
                + "PRODUCTNAME=" + elementVO.getProductname() + "PRIVATEKEY=" + elementVO.getPrivateKey();
        sign = sign + MessageFormat.format("LICENSESIGN={0}", SecretKeyhepler.encrypt(sign.getBytes(), publicKey));
        elementVO.setLicensesign(sign);
        String elementString = JSONObject.toJSONString(elementVO);
        //将写入的内容整体加密替换
        byte[] encrypt = SecretKeyhepler.encrypt(elementString.getBytes(), publicKey);
        String license = Base64.getEncoder().encodeToString(encrypt);
        return license;
    }

    /****
     * 解密
     * @param publicKey
     * @return
     */
    public static LicenseVO LicenseDecrypt(String publicKey, String liccontent) {
        LicenseVO elementVO = new LicenseVO();
        try {
            byte[] bytes = Base64.getDecoder().decode(liccontent);
            byte[] plainText = SecretKeyhepler.decrypt(bytes, publicKey);
            String result = SecretKeyhepler.byteToString(plainText);
            if (!StringUtils.isEmpty(result)) {
                elementVO = JSON.parseObject(result, LicenseVO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elementVO;
    }

    /***
     * 验证license
     * @return
     * @throws IOException
     */
    public static ApiResult verifyLocalLicense(String publicKey, String license) {
        ApiResult result = ApiResult.success();
        try {
            LicenseVO elementVO = LicenseDecrypt(publicKey, license);
            if (elementVO == null || elementVO.getExpireday() == null) {
                return ApiResult.error("解析失败");
            }
            String localMac = getLocalMac(InetAddress.getLocalHost());
            if (StringUtils.isEmpty(elementVO.getMacadress()) || !elementVO.getMacadress().equalsIgnoreCase(localMac)) {
                return ApiResult.error("mac地址为：" + localMac + ",当前机器未授权");
            }
            if (StringUtils.isEmpty(elementVO.getExpireday())) {
                return ApiResult.error("license文件异常");
            }
            Date expireday = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(elementVO.getExpireday());
            if (expireday.before(new Date())) {
                result.setMessage("当前系统已过期");
                return result;
            }
            String menuList = elementVO.getMenuList();
            if (StringUtils.isEmpty(menuList)) {
                return ApiResult.error("未获取到菜单权限");
            }
            result.setData(elementVO);
        } catch (SocketException e) {
            result = ApiResult.error("获取本地ip异常");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            result = ApiResult.error("获取本地ip异常");
            e.printStackTrace();
        } catch (ParseException e) {
            result = ApiResult.error("过期时间格式异常");
            e.printStackTrace();
        } catch (RuntimeException e) {
            result = ApiResult.error("未找到鉴权文件");
            e.printStackTrace();
        }
        return result;
    }

    public static String getLocalMac(InetAddress ia) throws SocketException {
        // TODO Auto-generated method stub
        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            if (str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

}
