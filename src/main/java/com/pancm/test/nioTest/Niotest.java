package com.pancm.test.nioTest;
/**
 * @author ZERO
 * @version 2017-5-10 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
public class Niotest {

	/**
	 * @param args
	 */
	
	 public static void main(String[] args)
	 {
	  FileChannel inChannel = null;
	  FileChannel outChannel = null;
	  try
	  {
	   File f = new File("Niotest.java");
	   //锟斤拷锟斤拷FileInputStream锟斤拷锟皆革拷锟侥硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷FileChannel
	   inChannel = new FileInputStream(f).getChannel();
	   //锟斤拷FileChannel锟斤拷锟饺拷锟斤拷锟斤拷映锟斤拷锟紹yteBuffer
	   MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY,
	    0 , f.length());
	   //使锟斤拷GBK锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷锟斤拷要锟斤拷锟斤拷锟斤拷
	   Charset charset = Charset.forName("GBK");
	   //锟斤拷锟侥硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟紽ileBuffer锟斤拷锟斤拷锟皆匡拷锟斤拷锟斤拷锟�
	   outChannel = new FileOutputStream("a.txt").getChannel();
	   //直锟接斤拷buffer锟斤拷锟斤拷锟斤拷全锟斤拷锟斤拷锟�
	   outChannel.write(buffer);
	   //锟劫次碉拷锟斤拷buffer锟斤拷clear()锟斤拷锟斤拷锟斤拷锟斤拷原limit锟斤拷position锟斤拷位锟斤拷
	   buffer.clear();
	   //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷(CharsetDecoder)锟斤拷锟斤拷
	   CharsetDecoder decoder = charset.newDecoder();
	   //使锟矫斤拷锟斤拷锟斤拷锟斤拷ByteBuffer转锟斤拷锟斤拷CharBuffer
	   CharBuffer charBuffer =  decoder.decode(buffer);
	   //CharBuffer锟斤拷toString锟斤拷锟斤拷锟斤拷锟皆伙拷取锟斤拷应锟斤拷锟街凤拷
	   System.out.println(charBuffer);
	  }
	  catch (IOException ex)
	  {
	   ex.printStackTrace();
	  }
	  finally
	  {
	   try
	   {
	    if (inChannel != null) {
			inChannel.close();
		}
	    if (outChannel != null) {
			outChannel.close();
		} 
	   }
	   catch (IOException ex)
	   {
	    ex.printStackTrace();
	   }
	  }
	 }
	

}
