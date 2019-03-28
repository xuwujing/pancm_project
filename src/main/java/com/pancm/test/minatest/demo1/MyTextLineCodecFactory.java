package com.pancm.test.minatest.demo1;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * The type My text line codec factory.
 *
 * @author ZERO
 * @version 2017 -3-28 上午11:39:50 编码格式工厂
 */
public class MyTextLineCodecFactory implements ProtocolCodecFactory {

    private Charset charset; // 编码格式
    private String delimiter; // 文本分隔符

    /**
     * Instantiates a new My text line codec factory.
     *
     * @param charset   the charset
     * @param delimiter the delimiter
     */
    public MyTextLineCodecFactory(Charset charset, String delimiter) {
        this.charset = charset;
        this.delimiter = delimiter;
    }
   
   
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return new MyTextLineCodecDecoder(charset, delimiter);
    }

   
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return new MyTextLineCodecEncoder(charset, delimiter);
    }

}
