package com.zans.base.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

/**
 * @author pancm
 * @Title: zans-mms-server
 * @Description: 微信相关工具类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/1/27
 */
public class WxUtil {



    private static final String KEY_ALGORITHM = "AES";
    private static final String ALGORITHM_STR = "AES/CBC/PKCS7Padding";
    private static Key key;
    private static Cipher cipher;


    public static String decryptData(String encryptDataB64, String sessionKeyB64, String ivB64) {

        return  decryptOfDiyIV(
                        Base64.decode(encryptDataB64),
                        Base64.decode(sessionKeyB64),
                        Base64.decode(ivB64)
           );
    }

    private static void init(byte[] keyBytes) {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        try {
            // 初始化cipher
//            cipher = Cipher.getInstance(ALGORITHM_STR, "BC");
            cipher = Cipher.getInstance(ALGORITHM_STR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param keyBytes      解密密钥
     * @param ivs           自定义对称解密算法初始向量 iv
     * @return 解密后的字节数组
     */
    private static String decryptOfDiyIV(byte[] encryptedData, byte[] keyBytes, byte[] ivs) {
        byte[] encryptedText = null;
        init(keyBytes);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivs));
            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new String(encryptedText);
    }



    public static void main(String[] args) {
      String  encryptedData ="JEKjo12ygprUJuHNiWeeosm5O9zRVT4/rmlfrMgmlrYsgW6WzozNXwKf42doM5E4mEuklBdUvgGf8j76jKj74MFyHWjgIFERRP1Zk6g8UgIuEbFEkMMdhtaleTnOpaA7iA2/zFXJEthOcjQ7sU1tnXH4zXkoc/c9d6WZ+ac7KLAqJMRUuQVpmT4l6MbIhMMODX1Sqz5AGwBSsORVOpzoK1pcUSLD2bf8b926y7fNVVeHDhMs0SUEn83TYtYznnELrci1coXs8ZXp5aYMXXssVm5wpEIytQBo+M4VTLMaaAZDWUU3pJdN5Y0/Qav5hUTMC+FrEJRp8h+qjAKTBPklvgyLoJdjTXyT95k8Yk0g1HL5pL17yRhw6e/xu/+s1oDqsDyfsi9RNtEau7qToxWJqDSfjE4qum0W1B/YR1qxG7YRN9LV4o33PCfy7xEvcfRWaxD7G9I933/IxO+9kirSR1BMpjPMws4uRx/m45D8ZL0=";
      String iv = "sknzlr2E/SfRC4NXHJhgjA==";
      String sk = "OvnEZbtOXoph6cj+NemKqw==";
//      encryptedData = "bC6Y8Jjr3NyJOpGSE8hFX6e7mx8YcuxhvyWD+/v9XDjKaF9qKyGRsDPbrjnkFGgy/GEHF2xMQChBWf4QuDz1FYSRZi7zrK4QaCLTCRwrnGRSDadpwS5lDcrGvYXA3fRtUW5UUebBlN/f9O1ITjakaUqET0uyDXBPQ2JRIzO1ZJqPZWwZ3L63mKf/K1gVADpTE+pcKw/2kj07VEetTcajpk7hpMCOBRi9nVo06hvRvyczyR2J66oRZBwiLKOfCGk8KmqEG5DtzkhlC0+lBRxgs08e3J9vWxAa4eJcqHcIZjvoC6JFYP97w0Jej3kljNF76jsCTno+uX0LJr1GpANdalsY7vugkQ37Eqnf1apcGPam4pW62nmsD2Ai96YqpmuOTaWziGflq/MCvW+1XA7k2ZcLI53KePUvgWDnX4+CKZSWnyYW16f+dm/htIfMxXIBPpw1E1h73oxefU8eVyuCso7yPJOoG6vYs7v/Vk/XOxs=";
//      iv ="PzquLQ7JnfDP9CzLZeQD0A==";
      System.out.println(decryptData(encryptedData,sk,iv));


    }

}
