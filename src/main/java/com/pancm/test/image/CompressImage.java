package com.pancm.test.image;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: 图片压缩
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/2/22
 */
public class CompressImage {


    public static void main(String[] args) throws IOException {
        String toPic ="";
        String imagePath ="";
        resize(200, 200, toPic,imagePath);
    }


    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param w int 新宽度
     * @param h int 新高度
     */
    public static  void resize(int w, int h, String toPic,String fileName) throws IOException {
        File file = new File(fileName);// 读入文件
        BufferedImage img = ImageIO.read(file);
        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
        File destFile = new File(toPic);
        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
        // 可以正常实现bmp、png、gif转jpg
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image); // JPEG编码
        out.close();
    }
}
