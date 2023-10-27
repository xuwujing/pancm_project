package com.pancm.test.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileConverter {
    
    public static void main(String[] args) throws IOException {
//        String inputFilePath = "/path/to/input/file";
        String jpegFilePath = "D:\\aa\\11.jpeg";
        String txtFilePath = "D:\\aa\\2222.txt";
        String docFilePath = "D:\\aa\\test-lc-18612345678.docx";

        // 判断文件类型并进行相应的转换操作
        File inputFile = new File(jpegFilePath);
        String name  = inputFile.getName();
        String outputFilePath = name +"_"+"file.pdf";
        String fileType = getFileType(inputFile);
        switch (fileType) {
            case "jpg":
            case "jpeg":
                convertImageToPdf(inputFile, outputFilePath);
                break;
            case "png":
                convertImageToPdf(inputFile, outputFilePath);
                break;
            case "doc":
            case "docx":
                convertDocToPdf(inputFile, outputFilePath);
                break;
            case "ppt":
            case "pptx":
                convertPptToPdf(inputFile, outputFilePath);
                break;
            case "wps":
                // 进行wps文件转换成doc或docx后再进行pdf转化
                break;
            case "txt":
                convertTxtToPdf(inputFile, outputFilePath);
                break;
            default:
                throw new IllegalArgumentException("不支持的文件类型: " + fileType);
        }
    }

    // 获取文件类型
    private static String getFileType(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    // 将图片转换为PDF格式
    private static void convertImageToPdf(File inputFile, String outputFilePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        switch (getFileType(inputFile)) {
            case "jpg":
            case "jpeg":
                contentStream.drawImage(JPEGFactory.createFromStream(document, Files.newInputStream(inputFile.toPath())), 0, 0);
                break;
            case "png":
//                contentStream.drawImage(PNGFactory.createFromStream(document, Files.newInputStream(inputFile.toPath())), 0, 0);
                break;
            default:
                throw new IllegalArgumentException("不支持的图片类型: " + getFileType(inputFile));
        }
        contentStream.close();

        document.save(outputFilePath);
        document.close();
    }

    // 将Word文档转换为PDF格式
    private static void convertDocToPdf(File inputFile, String outputFilePath) throws IOException {
        // 使用Apache POI或JODConverter将doc/docx转换为PDF格式
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        WordExtractor extractor = new WordExtractor(new FileInputStream(inputFile));
        String text = extractor.getText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.newLineAtOffset(100, 700);

        for (String line : text.split("\\r?\\n")) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -15);
        }

        contentStream.endText();
        contentStream.close();
        document.save(outputFilePath);
        document.close();
    }

    // 将PowerPoint文档转换为PDF格式
    private static void convertPptToPdf(File inputFile, String outputFilePath) throws IOException {
        // 使用Apache POI或JODConverter将ppt/pptx转换为PDF格式
    }

    // 将文本文件转换为PDF格式
    private static void convertTxtToPdf(File inputFile, String outputFilePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.newLineAtOffset(100, 700);

        Files.lines(inputFile.toPath())
                .forEach(line -> {
                    try {
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(0, -15);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        contentStream.endText();
        contentStream.close();

        document.save(outputFilePath);
        document.close();
    }
}
