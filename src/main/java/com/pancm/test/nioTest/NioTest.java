package com.pancm.test.nioTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/12/24
 */
public class NioTest {


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            File f = new File("NioTest.java");
            inChannel = new FileInputStream(f).getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY,
                    0, f.length());
            Charset charset = Charset.forName("GBK");
            outChannel = new FileOutputStream("a.txt").getChannel();
            outChannel.write(buffer);
            buffer.clear();
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer);
            System.out.println(charBuffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}
