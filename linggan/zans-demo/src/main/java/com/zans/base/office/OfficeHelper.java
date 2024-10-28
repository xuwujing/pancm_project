package com.zans.base.office;

import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author xv
 * @since 2020/3/21 10:28
 */
public class OfficeHelper {

    /**
     * 判断是否office文件
     * @param inputStream
     * @return
     */
    public static Boolean isOfficeFile(InputStream inputStream){
        boolean result = false;
        try {
            FileMagic fileMagic = FileMagic.valueOf(inputStream);
            if (Objects.equals(fileMagic, FileMagic.OLE2)||Objects.equals(fileMagic, FileMagic.OOXML)){
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断是否office文件
     * @param file
     * @return
     * @throws IOException
     */
    public static Boolean isOfficeFile(MultipartFile file) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream())) {
			boolean result = false;
			result = isOfficeFile(bufferedInputStream);
			return result;
		}
    }

}
