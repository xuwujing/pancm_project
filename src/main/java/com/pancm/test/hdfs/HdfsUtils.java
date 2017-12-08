package com.pancm.test.hdfs;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public class HdfsUtils {
    
    /**
     * @return
     * @throws Exception
     */
    public static FileSystem getFileSystem() throws Exception{
        
        Configuration conf = new Configuration() ;
        conf.set("fs.defaultFS", "hdfs://192.169.0.23:8020");
        
        FileSystem fileSystem = FileSystem.get(conf) ;
        
        return fileSystem ;
    }
    /**
     * @param pOpenUri
     * @param pUser
     * @return
     * @throws Exception
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public static FileSystem getFileSystemByUser(String pOpenUri,String pUser) throws Exception, InterruptedException, URISyntaxException{
        
        Configuration conf = new Configuration() ;
        conf.set("fs.defaultFS", "hdfs://192.168.1.109:8020");
        
        FileSystem fileSystem = FileSystem.get(new URI(pOpenUri), conf, pUser) ;
        
        return fileSystem ;
        
    }
    
    /**
     * @param pUser
     * @return
     * @throws Exception
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public static FileSystem getFileSystemByUser(String pUser) throws Exception, InterruptedException, URISyntaxException{
        
        String fileUri = "/home/test/test.txt" ;
        
        Configuration conf = new Configuration() ;
        conf.set("fs.defaultFS", "hdfs://192.169.0.23:8020");
        
        FileSystem fileSystem = FileSystem.get(new URI(fileUri), conf, pUser) ;
        
        return fileSystem ;
        
    }
    
    

}
