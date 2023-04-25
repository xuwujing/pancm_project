//package com.pancm.test.pdf;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
//import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.poi.hslf.usermodel.HSLFSlideShow;
//import org.apache.poi.hwpf.converter.WordToPDFConverter;
//import org.apache.poi.hwpf.usermodel.HWPFDocument;
//import org.apache.poi.xslf.usermodel.XMLSlideShow;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//
//public class DocPptToPdfConverter {
//
//    public static void main(String[] args) throws IOException {
//        convertDocToPdf("input.doc", "output.pdf");
//        convertPptToPdf("input.ppt", "output.pdf");
//    }
//
//    private static void convertDocToPdf(String inputFile, String outputFile) throws IOException {
//        HWPFDocument document = new HWPFDocument(new FileInputStream(inputFile));
//        WordToPDFConverter converter = new WordToPDFConverter(PDDocument.load(new File(outputFile)));
//        converter.processDocument(document);
//        PDDocument pdfDocument = converter.getDocument();
//        pdfDocument.save(outputFile);
//        pdfDocument.close();
//        document.close();
//    }
//
//    private static void convertPptToPdf(String inputFile, String outputFile) throws IOException {
//        HSLFSlideShow ppt = new HSLFSlideShow(new FileInputStream(inputFile));
//        XMLSlideShow pptx = new XMLSlideShow(new FileInputStream(inputFile));
//        PDDocument pdfDocument = new PDDocument();
//        for (int i = 0; i < ppt.getSlides().size(); i++) {
//            PDPage page = new PDPage();
//            pdfDocument.addPage(page);
//            ppt.getSlides().get(i).draw(new PPTX2PDFGraphics(pdfDocument, page));
//        }
//        for (int i = 0; i < pptx.getSlides().size(); i++) {
//            PDPage page = new PDPage();
//            pdfDocument.addPage(page);
//            pptx.getSlides().get(i).draw(new PPTX2PDFGraphics(pdfDocument, page));
//        }
//        pdfDocument.save(outputFile);
//        ppt.close();
//        pptx.close();
//        pdfDocument.close();
//    }
//
//    private static class PPTX2PDFGraphics extends org.apache.poi.xslf.usermodel.DrawPaint {
//        private final PDDocument document;
//        private final PDPage page;
//
//        public PPTX2PDFGraphics(PDDocument document, PDPage page) {
//            super(null, null, null);
//            this.document = document;
//            this.page = page;
//        }
//
//        @Override
//        public void drawImage(java.awt.Image img, int x, int y, int w, int h) {
//            try {
//                PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true);
//                contentStream.drawImage(pdImageXObjectFromAwt(img), x, y, w, h);
//                contentStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private static PDImageXObject pdImageXObjectFromAwt(java.awt.Image img) throws IOException {
//        BufferedImage bimage = toBufferedImage(img);
//        return LosslessFactory.createFromImage(document, bimage);
//    }
//
//    private static BufferedImage toBufferedImage(java.awt.Image img) throws IOException {
//        if (img instanceof BufferedImage) {
//            return (BufferedImage) img;
//        }
//        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = bimage.createGraphics();
//        g.drawImage(img, 0, 0, null);
//        g.dispose();
//        return bimage;
//    }
//}
