package com.pancm.test.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 *  itextpdf 测试用例，参考:https://www.cnblogs.com/h--d/p/6150320.html
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/1/26
 */
public class ITextPdfTest {

    public static void main(String[] args) {
        try {
            createPdf();
            createPdfAndAddPhoto();
            createPdfAndAddTable();
            createPdfAndAddList();
            createPdfAndAddStyle();
            createPdfAndSetPwd();
            createPdfAndSetPerm();
            readPdfAndUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createPdf() throws DocumentException, FileNotFoundException {

        String path = "test1.pdf";
        // 1.新建document对象
        Document document = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

        // 3.打开文档
        document.open();

        // 4.添加一个内容段落
        document.add(new Paragraph("Hello World!"));

        //设置属性
        //标题
        document.addTitle("this is a title");
        //作者
        document.addAuthor("pancm");
        //主题
        document.addSubject("this is subject");
        //关键字
        document.addKeywords("Keywords");
        //创建时间
        document.addCreationDate();
        //应用程序
        document.addCreator("panchengming.com");

        //关闭文档
        document.close();
        //关闭书写器
        writer.close();

        System.out.println("创建pdf文件成功！pdf:"+path);
    }


    private static void createPdfAndAddPhoto() throws DocumentException, IOException {

        String path = "test2.pdf";
        // 1.新建document对象
        Document document = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

        // 3.打开文档
        document.open();

        // 4.添加一个内容段落
        document.add(new Paragraph("Hello World!"));

        String imagePath = "E:\\图片\\11.jpg";
        //图片1
        Image image1 = Image.getInstance(imagePath);
        //设置图片位置的x轴和y周
        image1.setAbsolutePosition(100f, 550f);
        //设置图片的宽度和高度
        image1.scaleAbsolute(200, 200);
        //将图片1添加到pdf文件中
        document.add(image1);

        //图片2
//        Image image2 = Image.getInstance(new URL("http://static.cnblogs.com/images/adminlogo.gif"));
//        //将图片2添加到pdf文件中
//        document.add(image2);

        //关闭文档
        document.close();
        //关闭书写器
        writer.close();

        System.out.println("创建pdf文件并添加图片成功！pdf:"+path);
    }

    /**
     * @Author pancm
     * @Description 创建一个pdf然后添加table
     * @Date  2021/1/26
     * @Param []
     * @return void
     **/
    private static void createPdfAndAddTable() throws DocumentException, IOException {

        String path = "test3.pdf";
        // 1.新建document对象
        Document document = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
        // 3.打开文档
        document.open();
        // 4.添加一个内容段落
        document.add(new Paragraph("Hello World!"));

        // 3列的表.
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100); // 宽度100%填充
        table.setSpacingBefore(10f); // 前间距
        table.setSpacingAfter(10f); // 后间距

        ArrayList<PdfPRow> listRow = table.getRows();
        //设置列宽
        float[] columnWidths = { 1f, 2f, 3f };
        table.setWidths(columnWidths);

        //行1
        PdfPCell cells1[]= new PdfPCell[3];
        PdfPRow row1 = new PdfPRow(cells1);

        //单元格
        cells1[0] = new PdfPCell(new Paragraph("111"));//单元格内容
        cells1[0].setBorderColor(BaseColor.BLUE);//边框验证
        cells1[0].setPaddingLeft(20);//左填充20
        cells1[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells1[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中

        cells1[1] = new PdfPCell(new Paragraph("222"));
        cells1[2] = new PdfPCell(new Paragraph("333"));

        //行2
        PdfPCell cells2[]= new PdfPCell[3];
        PdfPRow row2 = new PdfPRow(cells2);
        cells2[0] = new PdfPCell(new Paragraph("444"));

        //把第一行添加到集合
        listRow.add(row1);
        listRow.add(row2);
        //把表格添加到文件中
        document.add(table);

        //关闭文档
        document.close();
        //关闭书写器
        writer.close();
        System.out.println("创建pdf文件并添加table成功！pdf:"+path);
    }

    /**
     * @Author pancm
     * @Description 创建一个pdf然后添加列表
     * @Date  2021/1/26
     * @Param []
     * @return void
     **/
    private static void createPdfAndAddList() throws DocumentException, IOException {

        String path = "test4.pdf";
        // 1.新建document对象
        Document document = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
        // 3.打开文档
        document.open();
        // 4.添加一个内容段落
        document.add(new Paragraph("Hello World!"));

        // 3列的表.
        //添加有序列表
        List orderedList = new List(List.ORDERED);
        orderedList.add(new ListItem("Item one"));
        orderedList.add(new ListItem("Item two"));
        orderedList.add(new ListItem("Item three"));
        document.add(orderedList);

        //关闭文档
        document.close();
        //关闭书写器
        writer.close();
        System.out.println("创建pdf文件并添加table成功！pdf:"+path);
    }

    /**
     * @Author pancm
     * @Description 创建一个pdf然后添加样式
     *  需要itext-asian架包
     * @Date  2021/1/26
     * @Param []
     * @return void
     **/
    private static void createPdfAndAddStyle() throws DocumentException, IOException {

        String path = "test5.pdf";
        // 1.新建document对象
        Document document = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
        // 3.打开文档
        document.open();
        // 4.添加一个内容段落
        document.add(new Paragraph("Hello World!"));


        //中文字体,解决中文不能显示问题
        BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);

        //蓝色字体
        Font blueFont = new Font(bfChinese);
        blueFont.setColor(BaseColor.BLUE);
        //段落文本
        Paragraph paragraphBlue = new Paragraph("paragraphOne blue front", blueFont);
        document.add(paragraphBlue);

        //绿色字体
        Font greenFont = new Font(bfChinese);
        greenFont.setColor(BaseColor.GREEN);
        //创建章节
        Paragraph chapterTitle = new Paragraph("段落标题xxxx", greenFont);
        Chapter chapter1 = new Chapter(chapterTitle, 1);
        chapter1.setNumberDepth(0);

        Paragraph sectionTitle = new Paragraph("部分标题", greenFont);
        Section section1 = chapter1.addSection(sectionTitle);

        Paragraph sectionContent = new Paragraph("部分内容", blueFont);
        section1.add(sectionContent);

        //将章节添加到文章中
        document.add(chapter1);

        //关闭文档
        document.close();
        //关闭书写器
        writer.close();
        System.out.println("创建pdf文件并设置样式成功！pdf:"+path);
    }


    /**
     * @Author pancm
     * @Description 创建一个pdf然后设置密码
     *  需要bcprov-jdk15on架包
     * @Date  2021/1/26
     * @Param []
     * @return void
     **/
    private static void createPdfAndSetPwd() throws DocumentException, IOException {

        String path = "test6.pdf";
        // 1.新建document对象
        Document document = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

        //用户密码
        String userPassword = "123456";
        //拥有者密码
        String ownerPassword = "hd";
        writer.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING,
                PdfWriter.ENCRYPTION_AES_128);

        // 打开文件
        document.open();

        //添加内容
        document.add(new Paragraph("password !!!!"));

        //关闭文档
        document.close();
        //关闭书写器
        writer.close();
        System.out.println("创建pdf文件并设置密码成功！pdf:"+path);
    }


    /**
     * @Author pancm
     * @Description 创建一个pdf然后设置权限
     *  需要bcprov-jdk15on架包
     * @Date  2021/1/26
     * @Param []
     * @return void
     **/
    private static void createPdfAndSetPerm() throws DocumentException, IOException {

        String path = "test7.pdf";
        // 1.新建document对象
        Document document = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

        // 只读权限
        writer.setEncryption("".getBytes(), "".getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);

        // 打开文件
        document.open();

        //添加内容
        document.add(new Paragraph("password !!!!"));
        //关闭文档
        document.close();
        //关闭书写器
        writer.close();
        System.out.println("创建pdf文件并设置权限成功！pdf:"+path);
    }


    /**
     * @Author pancm
     * @Description 读取一个pdf然后修改
     * @Date  2021/1/26
     * @Param []
     * @return void
     **/
    private static void readPdfAndUpdate() throws DocumentException, IOException {

        String path = "test1.pdf";
        String path2 = "test8.pdf";
        //读取pdf文件
        PdfReader pdfReader = new PdfReader(path);
        String imagePath = "E:\\图片\\11.jpg";
        //修改器
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(path2));

        Image image = Image.getInstance(imagePath);
        image.scaleAbsolute(50, 50);
        image.setAbsolutePosition(0, 700);

        for(int i=1; i<= pdfReader.getNumberOfPages(); i++)
        {
            PdfContentByte content = pdfStamper.getUnderContent(i);
            content.addImage(image);
        }

        pdfStamper.close();

        System.out.println("创建pdf文件并修改成功！pdf:"+path);
    }


}
