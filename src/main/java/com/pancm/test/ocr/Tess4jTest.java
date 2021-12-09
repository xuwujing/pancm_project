package com.pancm.test.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author beixing
 * @Title: pancm_project
 * @Description: ocr图片文字识别
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/26
 */
public class Tess4jTest {


    public static void main(String[] args) throws IOException {
        // 创建实例
        String path = "111.png";
        ITesseract instance = new Tesseract();
        // 设置识别语言
        instance.setLanguage("chi_sim");

        // 设置识别引擎
        instance.setOcrEngineMode(0);
        // 读取文件
        BufferedImage image = ImageIO.read(Tess4jTest.class.getResourceAsStream(path));
        try {

            // 识别
            String result = instance.doOCR(image);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }


    }
}
