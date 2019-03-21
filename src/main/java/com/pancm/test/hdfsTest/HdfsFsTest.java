package com.pancm.test.hdfsTest;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * The type Hdfs fs test.
 */
public class HdfsFsTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        String fileUri = "/home/test/test.txt";
        String fileOutputUrl = "/home/test/out.txt";
        try {

            writeFileToHdfs(fileUri, fileOutputUrl);
            System.out.println("DONE!");

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /**
     * Write file to hdfs.
     *
     * @param pOpenUri   the p open uri
     * @param pOutputUrl the p output url
     * @throws Exception the exception
     */
    public static void writeFileToHdfs(String pOpenUri, String pOutputUrl)
            throws Exception {

        FileSystem fileSystem = null;
        FSDataInputStream fileInputStream = null;
        FSDataOutputStream fileOutputStream = null;
        int buffSize = 4096;

        try {

            fileSystem = HdfsUtils.getFileSystem();
            fileInputStream = fileSystem.open(new Path(pOpenUri));
            fileOutputStream = fileSystem.create(new Path(pOutputUrl));
            IOUtils.copyBytes(fileInputStream, fileOutputStream, buffSize,
                    false);

        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeStream(fileInputStream);

            IOUtils.closeStream(fileOutputStream);
        }

    }

}