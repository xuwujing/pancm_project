package com.pancm.test.hdfsTest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * The type Hdfs utils.
 */
public class HdfsUtils {

    /**
     * Gets file system.
     *
     * @return file system
     * @throws Exception the exception
     */
    public static FileSystem getFileSystem() throws Exception{
        
        Configuration conf = new Configuration() ;
        conf.set("fs.defaultFS", "hdfs://192.169.0.23:8020");
        
        FileSystem fileSystem = FileSystem.get(conf) ;
        
        return fileSystem ;
    }

    /**
     * Gets file system by user.
     *
     * @param pOpenUri the p open uri
     * @param pUser    the p user
     * @return file system by user
     * @throws Exception            the exception
     * @throws InterruptedException the interrupted exception
     * @throws URISyntaxException   the uri syntax exception
     */
    public static FileSystem getFileSystemByUser(String pOpenUri,String pUser) throws Exception, InterruptedException, URISyntaxException{
        
        Configuration conf = new Configuration() ;
        conf.set("fs.defaultFS", "hdfs://192.168.1.109:8020");
        
        FileSystem fileSystem = FileSystem.get(new URI(pOpenUri), conf, pUser) ;
        
        return fileSystem ;
        
    }

    /**
     * Gets file system by user.
     *
     * @param pUser the p user
     * @return file system by user
     * @throws Exception            the exception
     * @throws InterruptedException the interrupted exception
     * @throws URISyntaxException   the uri syntax exception
     */
    public static FileSystem getFileSystemByUser(String pUser) throws Exception, InterruptedException, URISyntaxException{
        
        String fileUri = "/home/test/test.txt" ;
        
        Configuration conf = new Configuration() ;
        conf.set("fs.defaultFS", "hdfs://192.169.0.23:8020");
        
        FileSystem fileSystem = FileSystem.get(new URI(fileUri), conf, pUser) ;
        
        return fileSystem ;
        
    }
    
    

}
