package com.pancm.test.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/1/26
 */
public class PdfBoxTest {

    public static void main(String[] args) throws IOException {
//        String formTemplate = "E:/module.pdf";
        String formTemplate = "module.pdf";
    // 定义文档对象
        PDDocument document = new PDDocument();
    // 定义一页，大小A4
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
    // 获取字体
        PDType0Font font = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\Arial.ttf"));
    // 定义页面内容流
        PDPageContentStream stream = new PDPageContentStream(document, page);
    // 设置字体及文字大小
        stream.setFont(font, 12);
    // 设置画笔颜色
        stream.setNonStrokingColor(Color.BLACK);
        // 添加矩形
        stream.addRect(29, 797, 100, 14);
     // 填充矩形
        stream.fill();
        stream.setNonStrokingColor(Color.BLACK);
        // 文本填充开始
        stream.beginText();
          // 设置行距
        stream.setLeading(18f);
            // 设置文字位置
        stream.newLineAtOffset(30, 800);
         // 填充文字
        stream.showText("11");
        // 换行
        stream.newLine();
        stream.showText("22");
        stream.newLine();
        stream.showText("33");
    // 文本填充结束
        stream.endText();
    // 关闭流
        stream.close();
    // 保存
        document.save(formTemplate);
    // 释放资源
        document.close();
    }
}
