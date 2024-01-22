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
            // Create a document from the word file
            XWPFDocument document = new XWPFDocument(Files.newInputStream(wordFile.toPath()));
            // Create a word extractor to get the text from the document
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);

            StringBuilder markdownContent = new StringBuilder();

            // Iterate through all the paragraphs in the document
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                // Append the paragraph text to the markdownContent
                markdownContent.append(paragraph.getText()).append("\n");
            }
            // Iterate through all the pictures in the document
            for (XWPFPictureData picture : document.getAllPictures()) {
                // Get the picture data
                byte[] pictureData = picture.getData();
                // Save pictureData to file and include link in markdownContent
            }
            // Write the markdownContent to a file
            FileWriter writer = new FileWriter("output.md");
            writer.write(markdownContent.toString());
            writer.close();

            System.out.println("Word document converted to Markdown successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
