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
        List<String> lines = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file)) {
            StringBuilder content = new StringBuilder();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                content.append(new String(buffer, 0, len));
            }
            String[] words = content.toString().split("\\s+");
            Collections.addAll(lines, words);
        }
        return lines;
    }
}
