package com.pancm.test.wordTest;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class WordToMarkdownConverter {

    public static void main(String[] args) {
        try {
            File wordFile = new File("input.docx");
            XWPFDocument document = new XWPFDocument(Files.newInputStream(wordFile.toPath()));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);

            StringBuilder markdownContent = new StringBuilder();

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                markdownContent.append(paragraph.getText()).append("\n");
            }

            for (XWPFPictureData picture : document.getAllPictures()) {
                byte[] pictureData = picture.getData();
                // Save pictureData to file and include link in markdownContent
            }

            FileWriter writer = new FileWriter("output.md");
            writer.write(markdownContent.toString());
            writer.close();

            System.out.println("Word document converted to Markdown successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
