package com.pancm.test.xdecoder.vo;



import lombok.Data;

import java.io.Serializable;



/**
 * 加密的请求基础类，需要加密的请求需要继承此类
 */
@Data
public class XDecoderVO implements Serializable {


    protected String encryptionJson;


}
