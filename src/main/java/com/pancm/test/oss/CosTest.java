package com.pancm.test.oss;
 
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.File;

/**
 * @Author pancm
 * @Description 腾讯云COS服务上传测试
 * @Date  2024/3/8
 * @Param
 * @return
 **/
public class CosTest {
 
	private String secretId = "xxxx";
 
	private String secretKey ="xxxx";
 
	private String region ="ap-guangzhou";
 
	private String bucketName= "test-1307462009";
 
	private String path ="/202403/01";
 
	public  COSClient initCOSClient(){
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
		Region region = new Region(this.region);
		ClientConfig clientConfig = new ClientConfig(region);
		// 生成 cos 客户端。
		COSClient cosClient = new COSClient(cred, clientConfig);
		return cosClient;
	}

	public static void main(String[] args) {
		String filePath ="D:\\图片\\项目图片\\registerCar-min.png";
		CosTest demo1ApplicationTests = new CosTest();
		demo1ApplicationTests.upLoad(filePath);
	}

	/**
	 * 上传文件
	 */

	public  void upLoad(String filePath){
		try {
			// 指定要上传的文件
			File localFile = new File(filePath);
			// 指定要上传到 COS 上对象键
            String key = getFileKey(filePath);
            System.out.println(key + "key----------------------");
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
			COSClient cosClient1 = initCOSClient();
			PutObjectResult putObjectResult = cosClient1.putObject(putObjectRequest);
            // 设置权限(公开读)
            cosClient1.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            System.out.println("url------------" + path + "/"+ key);
		} catch (CosServiceException serverException) {
			serverException.printStackTrace();
		} catch (CosClientException clientException) {
			clientException.printStackTrace();
		}
	}
 
 
	/**
	 * 生成文件路径
	 *
	 * @return
	 */
	private static String getFileKey(String originalfileName) {
		String filePath = "test/";
		//1.获取后缀名 2.去除文件后缀 替换所有特殊字符
		String fileType = originalfileName.substring(originalfileName.lastIndexOf("."));
		String fileStr = StrUtil.removeSuffix(originalfileName, fileType).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "_");
		filePath +=  new DateTime().toString("yyyyMMddHHmmss") + "_" + fileStr + fileType;
		return filePath;
	}
 
}