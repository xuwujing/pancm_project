package com.zans.vo;

import lombok.Data;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/18
 */
@Data
public class NetDiskFile {

    private String fileName;

    private String fileSize;

    private String updateTime;

    private String downLoadPath;

}
