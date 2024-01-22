package com.pancm.test.wordTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author pancm
 * @Description word文件切割
 * @Date 2023/11/15
 * @Param
 **/
public class WordSplitter {
    public static void main(String[] args) throws IOException {
        File file = new File("input.docx");
        List<String> lines = splitWordFile(file);
        for (String line : lines) {
            System.out.println(line);
        }
    }
   public static List<String> splitWordFile(File file) throws IOException {
        //Create a list to store the words
        List<String> lines = new ArrayList<>();
        //Create a new file input stream to read the file
        try (FileInputStream fis = new FileInputStream(file)) {
            //Create a string builder to store the content of the file
            StringBuilder content = new StringBuilder();
            //Create a byte array to store the content of the file
            byte[] buffer = new byte[1024];
            //Declare a variable to store the length of the content
            int len;
            //Loop through the file and read the content
            while ((len = fis.read(buffer)) != -1) {
                //Append the content to the string builder
                content.append(new String(buffer, 0, len));
            }
            //Split the content into an array of words
            String[] words = content.toString().split("\\s+");
            //Add all the words to the list
            Collections.addAll(lines, words);
        }
        //Return the list of words
        return lines;
    }
}
