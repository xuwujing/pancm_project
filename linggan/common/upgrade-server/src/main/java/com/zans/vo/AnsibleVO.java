package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author beixing
 * @Title: upgrade-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022/3/15
 */
@Data
public class AnsibleVO {

    private String srcPath;

    private String dstPath;

    private String superName;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
