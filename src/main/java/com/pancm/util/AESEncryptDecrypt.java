package com.pancm.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * The type Aes encrypt decrypt.
 *
 * @author pancm
 * @Title: AESEncryptDecrypt
 * @Description:AES加解密
 * @Version:1.0.0
 * @date 2018年7月23日
 */
public class AESEncryptDecrypt {

	private static final String AESKEY = "m!@$%s^&*a)_e+s.";
	private static final Logger logger = LoggerFactory.getLogger(AESEncryptDecrypt.class);

	private static final char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    /**
     * AES加密
     *
     * @param content the content
     * @return string
     */
    public static String encode(String content) {
		try {
			// 获取用户账号字节数组
			byte[] src = content.getBytes("GBK");
			// 返回加密后的字节数组
			byte[] b = aesEncrypt(src, AESKEY.getBytes("GBK"));
			return parseByte2HexStr(b);
		} catch (Exception e) {
			logger.error("加密失败!");
		}
		// 异常，返回null结果
		return null;
	}

    /**
     * AES解密
     *
     * @param content the content
     * @return string
     */
    public static String decode(String content) {
		try {
			// 进行AES解密
			byte[] src = parseHexStr2Byte(content);
			byte[] decodeBytes = aesDecrypt(src, AESKEY.getBytes("GBK"));
			return new String(decodeBytes, "GBK");
		} catch (Exception e) {
			logger.error("解密失败!");
		}
		// 返回结果
		return null;
	}

	/***
	 * AES加密实现方法 采用GBK编码
	 * 
	 * @param content
	 *            加密内容字节数组
	 * @param
	 *
	 * @return 密文字节数组
	 */
	private static byte[] aesEncrypt(byte[] content, byte[] aesKey) {
		try {
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
			byte[] result = cipher.doFinal(tmpContent);
			// 返回结果
			return result; // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * AES解密实现方法
	 * 
	 * @param content
	 *            密文字节数组
	 * @param aesKey
	 *            密钥字节数组
	 * @return 明文字节数组
	 */
	private static byte[] aesDecrypt(byte[] content, byte[] aesKey) {
		// 如果密钥不是16个字节，返回空
		if (aesKey == null || aesKey.length != 16) {
			return null;
		}
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 将字节数组转化成十六进制字符串
	 * 
	 * @param buf
	 *            字节数组
	 * @return 十六进制字符串
	 */
	private static String parseByte2HexStr(byte buf[]) {
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

	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}


    /**
     * Encoding string.
     *
     * @param code the code
     * @param key  the key
     * @return the string
     */
    @SuppressWarnings("static-access")
	public static String encoding(String code,String key) {
		if(null == code){
			return null;
		}
		String text = null;
		//MD5生成密文
		try {
			String ciphertext = MD5(code);
			String md5Text = ciphertext+ code;
			//DES加密MD5字段+明文
			byte[] key_ = getKey(key);
			SecretKey desKey = getDesKey(key_);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE,desKey);
			byte[] desText = cipher.doFinal(md5Text.getBytes("utf-8"));
			//BASE64编码
//			BASE64Encoder b64 = new BASE64Encoder();
//			text = b64.encode(desText);
			text =new Base64().encodeBase64String(desText);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return text;
	}

    /**
     * 解密需要解密的密文
     *
     * @param code 密文
     * @param key  the key
     * @return the string
     * @key 密钥
     */
    public static String decode(String code,String key) {
		if(null == code || null == key){
			return null;
		}
		String plainText = null;
		try{
			//BASE64 DECODE
//			BASE64Decoder b64 = new BASE64Decoder();
//			byte[] inputStream = b64.decodeBuffer(code);
			//BASE64 DECODE
			byte[] inputStream = new Base64().decode(code);
			//DESj解密
			byte[] key_ = getKey(key);
			SecretKey desKey = getDesKey(key_);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE,desKey);
			byte[] desText = cipher.doFinal(inputStream);
			plainText = new String(desText,"UTF-8");
			//因为MD5是固定的32byte的长度，这里截取32byte之后的内容即为明文内容
			plainText = plainText.substring(32, plainText.length());
		}catch(Exception e){
			e.printStackTrace();
			return null;
			
		}
		return plainText;
	}

    /**
     * 密钥是一个长度16、由16进制字符组成的字符串，如：1234567890ABCDEF
     * 使用时，相临的两位理解为一个16进制数的明文，然后转换为实际使用的8位密钥。
     *
     * @param key 加密或解密密钥,如：1234567890ABCDEF
     * @return 返回8bytes类型数组一共是64位 byte [ ]
     */
    public static byte[] getKey(String key){
		byte[] key_ = new byte[8];
		//没两位代表一个16进制明文，转换成一个byte
		for(int i = 0;i < 8; i++){
			int t =  Integer.parseInt(key.substring(i*2,i*2+2),16);
			if(t > Byte.MAX_VALUE){
				t = t- 256;
			}
			key_[i] = (byte)t;
		}
		return key_;
	}
	/**
	 * 得到DES算法的64位密钥
	 * @param key 8个byte数组
	 * @return 对应的DES密钥
	 */
	private static SecretKey getDesKey(byte[] key){
		DESKeySpec dks;
		SecretKeyFactory keyFactory;
		SecretKey secretKey = null;
		try {
			dks = new DESKeySpec(key);
			keyFactory = SecretKeyFactory.getInstance("DES");
			secretKey = keyFactory.generateSecret(dks);
		} catch (Exception e) {
			e.printStackTrace();
		}   
	  
        return secretKey;
	}

    /**
     * Get sign string.
     *
     * @param seqid     the seqid
     * @param timestamp the timestamp
     * @return the string
     * @date 2018年7月9日 下午4:13:28
     * @description 签名加密 bizcode timestamp
     */
    public static String getSign(String seqid, String timestamp){
		String sign = "";
        try {
			byte[] input = (seqid + timestamp+"MBOSS").getBytes("UTF-8");
	        // 获得MD5摘要算法的 MessageDigest 对象
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        // 使用指定的字节更新摘要
	        md.update(input);
	        // 获得密文
	        input = md.digest();
	        // 把密文转换成十六进制的字符串形式
	        int j = input.length;
	        char str[] = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	            byte byte0 = input[i];
	            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            str[k++] = hexDigits[byte0 & 0xf];
	        }
	        return new String(str).toLowerCase();
        } catch (Exception e) {
        	e.printStackTrace();
		}
        return sign;
	}
	
	private static String MD5(String plainText){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			String result = buf.toString();
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		} 
	}
}
