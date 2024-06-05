package com.pancm.test.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * @author pancm
 * @Title: mboss
 * @Description: 加密实现类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/1/9
 */
public class BaseCodingHandler implements IBaseCodingHandler {


    @Override
    public String getToken(String userid, String pwd, String time) {
        return BCryptEncoder(userid+COMMA_SIGN+pwd+COMMA_SIGN+time);
    }


    @Override
    public boolean matchesToken(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword,encodedPassword);
    }


    @Override
    public String getEncode(String key, String rawPassword, String time) {
        return encoding(key,rawPassword,time);
    }

    @Override
    public String getDecode(String key, String encodedPassword, String time) {
        return decode(key,encodedPassword,time);
    }



    /**
     * @Author pancm
     * @Description hash加密
     * @Date  2020/1/9
     * @Param [rawPassword]
     * @return java.lang.String
     **/
    private String BCryptEncoder(String rawPassword){
        return new BCryptPasswordEncoder().encode(rawPassword);
    }


    /**
     * 加密
     *
     * @param code 需要加密的密文
     */
    public String encoding( String key,String code,String time) {
        if (isEmpty(code)||isEmpty(time)||isEmpty(key)) {
            return null;
        }
        String text = null;
        try {
            String ciphertext = BCryptEncoder(time);
            String md5Text = ciphertext + code;
            String aesEncode = aesEncode(md5Text,key);
            text =  base64EnStr(aesEncode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return text;
    }

    /**
     * 解密需要解密的密文
     *
     * @param code
     *            密文
     * @key 密钥
     */
    public String decode(String key,String code,String time) {
        if (isEmpty(code)||isEmpty(time)||isEmpty(key)) {
            return null;
        }
        String plainText = null;
        try {

            String inputStream = base64DeStr(code);
            plainText= aesDecode(inputStream,key);
            plainText = plainText.substring(SUB_LENGTH);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return plainText;
    }

    /**
     * base64 加密
     * @param str
     * @return
     */
    public  String base64EnStr(String str) throws UnsupportedEncodingException {
        return Base64.encodeBase64String(str.getBytes(CODING));
    }

    /**
     * base64解密
     * @param encodeStr
     * @return
     */
    public static String base64DeStr(String encodeStr) throws UnsupportedEncodingException{
        return new String(Base64.decodeBase64(encodeStr),CODING);
    }




    public  String aesEncode(String password,String key)  {
        String result = null;
        byte[] passwordByte = new byte[0];
        try {
            passwordByte = password.getBytes(CODING);
            result = parseByte2HexStr(aesEncrypt(passwordByte, key.getBytes(CODING)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /***
     * AES解密方法
     * @param password
     *            密文密码
     * @return
     * @throws Exception
     */
    public static String aesDecode(String password,String key) {
        // 默认返回值为null
        String res = null;
        byte[] content = parseHexStr2Byte(password);
        // 进行AES解密
        byte[] decodeBytes = new byte[0];
        try {
            decodeBytes = aesDecrypt(content, key.getBytes(CODING));
            res = new String(decodeBytes, CODING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回结果
        return res;
    }

    /***
     * AES加密实现方法
     * @param content
     *            加密内容字节数组
     * @param aesKey
     *            密钥字节数组
     * @return 密文字节数组
     * @throws
     */
    private static byte[] aesEncrypt(byte[] content, byte[] aesKey) throws Exception {
        // 产生AES密钥
        SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
        // 设置加密模式与填充方法
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        // 加密内容字节数组
        byte[] byteContent = content;
        // 加密内容字节数组长度
        int len = byteContent.length;
        // 加密内容字节数组不足16个字节，进行补全
        int l = (len % 16 > 0) ? (16 - len % 16) : 0;
        // 新建补全后长度大小的字节数组
        byte[] tmpContent = new byte[byteContent.length + l];
        // 数组拷贝到补全后数组
        System.arraycopy(byteContent, 0, tmpContent, 0, len);
        // 初始化加密对象
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        // 加密成密文字节数组
        // 返回结果
        return cipher.doFinal(tmpContent); // 加密
    }


    private static byte[] aesDecrypt(byte[] content, byte[] aesKey) throws Exception {
        // 如果密钥不是16个字节，返回空
        if (aesKey == null || aesKey.length != 16) {
            return null;
        }
        // 产生AES密钥
        SecretKeySpec skp = new SecretKeySpec(aesKey, "AES");
        // 设置加密模式与填充方法
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        // 初始化解密对象
        cipher.init(Cipher.DECRYPT_MODE, skp);
        // 解密成明文字节数组
        byte[] original = cipher.doFinal(content);
        int i = 0;
        // 遍历字节数组
        for (i = 0; i < original.length; i++) {
            // 如果出现以0补全的字节，跳出循环
            if (original[i] == 0) {
                break;
            }
        }
        // 创建除去0补全长度的字节数组
        byte[] bbb = new byte[i];
        // 进行数组拷贝到去除0补全长度的字节数组
        System.arraycopy(original, 0, bbb, 0, i);
        // 返回结果
        return bbb;
    }

    /***
     * 将字节数组转化成十六进制字符串
     * @param buf
     *            字节数组
     * @return 十六进制字符串
     */
    public static String parseByte2HexStr(byte buf[]) {
        // 字符串缓存
        StringBuffer sb = new StringBuffer();
        // 遍历字节数组，转化成十六进制字符串
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            // 长度为1的前面加0补全
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            // 添加到字符串缓存中
            sb.append(hex);
        }
        // 返回结果
        return sb.toString();
    }

    /**
     * 将字符串转换成字节数组
     * @param hexStr
     * @return buf 字节数组
     *
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.isEmpty())
            return null;
        return getBytes(hexStr);
    }

    public static byte[] getBytes(String hexStr) {
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    private  boolean isEmpty(String str) {
        return (null == str || str.trim().isEmpty());
    }


    private static final String COMMA_SIGN = ",";
    private static final String CODING = "UTF-8";
    private static final int SUB_LENGTH = 60;


    public static void main(String[] args) {
        IBaseCodingHandler baseCodingHandler = new BaseCodingHandler();
        String userid="mboss_admin";
        String pwd="123";
        String key="m!@$%s^&*a)_e+s.";
        String time="20180516095645990";
        String msg="msg";

        String token = baseCodingHandler.getToken(userid,pwd,time);
        String token2 = userid+COMMA_SIGN+pwd+COMMA_SIGN+time;
        boolean isMatch = baseCodingHandler.matchesToken(token2,token);
        String msg2 = baseCodingHandler.getEncode(key,msg,time);
        String msg3 = baseCodingHandler.getDecode(key,msg2,time);
        System.out.println("token:"+token);
        System.out.println("isMatch:"+isMatch);
        System.out.println("msg:"+msg);
        System.out.println("msg2:"+msg2);
        System.out.println("msg3:"+msg3);
    }
}
