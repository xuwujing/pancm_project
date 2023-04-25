//package com.pancm.test.pdf;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.io.IOUtils;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.common.PDRectangle;
//import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
//import org.apache.pdfbox.rendering.PDFRenderer;
//import org.apache.poi.xslf.usermodel.XMLSlideShow;
//import org.apache.poi.xslf.usermodel.XSLFSlide;
//import org.apache.poi.xwpf.converter.pdf.PdfConverter;
//import org.apache.poi.xwpf.converter.pdf.PdfOptions;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//
///**
// * @author pancm
// * @Title: pancm_project
// * @Description: 转换pdf
// * @Version:1.0.0
// * @Since:jdk1.8
// * @date 2023/4/25
// */
//public class ConvertToPdf {
//
//
//    public static void main(String[] args) {
//        String fileUrl = "";
//    }
//
//
//
//    private void test1(String fileUrl){
//
//    }
//
//
//    private boolean isSupportedFileType(MultipartFile file) {
//        String[] supportedTypes = {"doc", "docx", "ppt", "pptx", "wps", "jpg", "jpeg", "png", "txt"};
//        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
//        if (Arrays.asList(supportedTypes).contains(fileExtension.toLowerCase())) {
//            return true;
//        }
//        return false;
//    }
//
//    private File convertToPdf(MultipartFile file) throws IOException {
//        PDDocument document = null;
//        try {
//            document = new PDDocument();
//            if ("doc".equalsIgnoreCase(FilenameUtils.getExtension(file.getOriginalFilename()))) {
//                // Convert .doc to .docx first
//                XWPFDocument docx = new XWPFDocument(file.getInputStream());
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                docx.write(out);
//                InputStream in = new ByteArrayInputStream(out.toByteArray());
//                file = new MockMultipartFile(file.getName(), file.getOriginalFilename().replace(".doc", ".docx"), file.getContentType(), in);
//            }
//            PDFRenderer renderer = new PDFRenderer(document);
//            if ("txt".equalsIgnoreCase(FilenameUtils.getExtension(file.getOriginalFilename()))) {
//                // Convert .txt to .html first
//                String html = IOUtils.toString(file.getInputStream(), String.valueOf(StandardCharsets.UTF_8));
//                InputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
//                file = new MockMultipartFile(file.getName(), file.getOriginalFilename().replace(".txt", ".html"), file.getContentType(), in);
//            }
//            if ("ppt".equalsIgnoreCase(FilenameUtils.getExtension(file.getOriginalFilename()))) {
//                // Convert .ppt to .pptx first
//                XMLSlideShow pptx = new XMLSlideShow(file.getInputStream());
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                pptx.write(out);
//                InputStream in = new ByteArrayInputStream(out.toByteArray());
//                file = new MockMultipartFile(file.getName(), file.getOriginalFilename().replace(".ppt", ".pptx"), file.getContentType(), in);
//            }
//            if ("pptx".equalsIgnoreCase(FilenameUtils.getExtension(file.getOriginalFilename()))) {
//                // Convert .pptx to .pdf
//                XMLSlideShow pptx = new XMLSlideShow(file.getInputStream());
//                XSLFSlideRenderer slideRenderer = new XSLFSlideRenderer(pptx);
//                for (XSLFSlide slide : pptx.getSlides()) {
//                    BufferedImage image = slideRenderer.renderSlide(slide);
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    ImageIO.write(image, "png", out);
//                    PDPage page = new PDPage(PDRectangle.A4);
//                    document.addPage(page);
//                    PDImageXObject pdImageXObject = PDImageXObject.createFromByteArray(document, out.toByteArray(), "img");
//                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                        contentStream.drawImage(pdImageXObject, 0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());
//                    }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//
//    private void toPdf(String suffix,MultipartFile file){
//        // 转换 doc、docx、wps 文件为 pdf 格式
//        if (suffix.equals("doc") || suffix.equals("docx") || suffix.equals("wps")) {
//            try (InputStream inputStream = file.getInputStream()) {
//                XWPFDocument document = new XWPFDocument(inputStream);
//                PdfOptions options = PdfOptions.create();
//                File outFile = new File("converted.pdf");
//                OutputStream out = new FileOutputStream(outFile);
//                PdfConverter.getInstance().convert(document, out, options);
//            } catch (IOException e) {
//                // 处理异常
//            }
//        }
//// 转换 ppt、pptx 文件为 pdf 格式
//        else if (suffix.equals("ppt") || suffix.equals("pptx")) {
//            try (InputStream inputStream = file.getInputStream()) {
//                XMLSlideShow ppt = new XMLSlideShow(inputStream);
//                PdfOptions options = PdfOptions.create();
//                File outFile = new File("converted.pdf");
//                OutputStream out = new FileOutputStream(outFile);
//                ppt.write(out);
//            } catch (IOException e) {
//                // 处理异常
//            }
//        }
//// 转换 jpg、jpeg、png 图片为 pdf 格式
//        else if (suffix.equals("jpg") || suffix.equals("jpeg") || suffix.equals("png")) {
//            try (InputStream inputStream = file.getInputStream()) {
//                BufferedImage image = ImageIO.read(inputStream);
//                Document document = new Document();
//                File outFile = new File("converted.pdf");
//                OutputStream out = new FileOutputStream(outFile);
//                PdfWriter.getInstance(document, out);
//                document.open();
//                Image iTextImage = Image.getInstance(image, null);
//                document.add(iTextImage);
//                document.close();
//            } catch (IOException | DocumentException e) {
//                // 处理异常
//            }
//        }
//// 转换 txt 文件为 pdf 格式
//        else if (suffix.equals("txt")) {
//            try (InputStream inputStream = file.getInputStream()) {
//                String text = new String(inputStream.readAllBytes());
//                Document document = new Document();
//                File outFile = new File("converted.pdf");
//                OutputStream out = new FileOutputStream(outFile);
//                PdfWriter.getInstance(document, out);
//                document.open();
//                Paragraph paragraph = new Paragraph(text);
//                document.add(paragraph);
//                document.close();
//            } catch (IOException | DocumentException e) {
//                // 处理异常
//            }
//        }
//
//
//    }
//}
