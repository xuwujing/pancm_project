package com.zans.base.util;


import com.google.common.collect.Lists;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

/***
 *
 * 对图片进行操作
 * @author chenzheng_java
 * @since 2011/7/29  
 */
public class ImageHelper {

    private static ImageHelper imageHelper = null;

    public static ImageHelper getImageHelper() {
        if (imageHelper == null) {
            imageHelper = new ImageHelper();
        }
        return imageHelper;
    }

    /**
     * 判断扩展名是否是excel扩展名
     * @param extension
     * @return
     */
    public static Boolean checkExtension(String extension){
        if (extension == null) {
            return false;
        }
        return Lists.newArrayList("gif", "GIF", "jpeg", "JPEG","jpg", "JPG", "png", "PNG").contains(extension);
    }

    /**
     * 判断扩展名是否是图片扩展名
     * @param file
     * @return
     */
    public static Boolean checkExtension(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }
        String extension = FileHelper.getFileExtension(fileName);
        if (extension == null) {
            return false;
        }
        return checkExtension(extension);
    }

    public static Boolean checkExtensions(MultipartFile[] files){
        for (MultipartFile file : files) {
            Boolean aBoolean = checkExtension(file);
            if (!aBoolean){
                return false;
            }
        }
        return true;
    }



    /***
     * 按指定的比例缩放图片 
     * @param sourceImagePath 源地址
     * @param destinationPath 改变大小后图片的地址
     * @param scale            缩放比例，如1.2  
     */
    public static void scaleImage(String sourceImagePath, String destinationPath, Double scale, String format) {

        File file = new File(sourceImagePath);
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(file);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            width = parseDoubleToInt(width * scale);
            height = parseDoubleToInt(height * scale);

            Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = outputImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();

            ImageIO.write(outputImage, format, new File(destinationPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     *	将图片缩放到指定的高度或者宽度
     *    @param sourceImagePath 图片源地址
     *    @param destinationPath 压缩完图片的地址
     *    @param width 缩放后的宽度
     *    @param height 缩放后的高度
     *    @param auto 是否自动保持图片的原高宽比例
     */
    public static void scaleImageWithParams(String sourceImagePath, String destinationPath, int width, int height,
                                            boolean auto) {

        try {
            String format = sourceImagePath.substring(sourceImagePath.lastIndexOf(".") + 1);
            File file = new File(sourceImagePath);
            BufferedImage bufferedImage = null;
            bufferedImage = ImageIO.read(file);
            if (auto) {
                ArrayList<Integer> paramsArrayList = getAutoWidthAndHeight(bufferedImage, width, height);
                width = paramsArrayList.get(0);
                height = paramsArrayList.get(1);
                System.out.println("自动调整比例，width=" + width + " height=" + height);
            }

            Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = outputImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            ImageIO.write(outputImage, format, new File(destinationPath));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /***
     *	将图片缩放到指定的高度或者宽度
     *    @param width 缩放后的宽度
     *    @param height 缩放后的高度
     *    @param auto 是否自动保持图片的原高宽比例
     */
    public static byte[] compressImage(byte[] imageByte, int width, int height,
                                            boolean auto) {
        byte[] smallImage = null;
        if (imageByte == null){
            return null;
        }
        ByteArrayInputStream byteInput = new ByteArrayInputStream(imageByte);
        try {
            BufferedImage bufferedImage = null;
            bufferedImage = ImageIO.read(byteInput);
            if (auto) {
                ArrayList<Integer> paramsArrayList = getAutoWidthAndHeight(bufferedImage, width, height);
                width = paramsArrayList.get(0);
                height = paramsArrayList.get(1);
            }

            Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = outputImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(outputImage, "png", out);
            smallImage = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return smallImage;

    }
    public static byte[] compressImage(byte[] imageByte,boolean auto){
        return compressImage(imageByte,60,60,auto);
    }


    /**
     *  * 将double类型的数据转换为int，四舍五入原则  *  * @param sourceDouble  * @return  
     */
    private static int parseDoubleToInt(Double sourceDouble) {
        int result = 0;
        result = sourceDouble.intValue();
        return result;
    }

    /***
     *  *  * @param bufferedImage 要缩放的图片对象  * @param width_scale 要缩放到的宽度
     *  * @param height_scale 要缩放到的高度  * @return 一个集合，第一个元素为宽度，第二个元素为高度  
     */
    private static ArrayList<Integer> getAutoWidthAndHeight(BufferedImage bufferedImage, int width_scale,
                                                            int height_scale) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        Double scale_w = getDot2Decimal(width_scale, width);
        
        Double scale_h = getDot2Decimal(height_scale, height);
        if (scale_w < scale_h) {
            arrayList.add(parseDoubleToInt(scale_w * width));
            arrayList.add(parseDoubleToInt(scale_w * height));
        } else {
            arrayList.add(parseDoubleToInt(scale_h * width));
            arrayList.add(parseDoubleToInt(scale_h * height));
        }
        return arrayList;

    }

    /***
     *  * 返回两个数a/b的小数点后三位的表示  * @param a  * @param b  * @return  
     */
    public static Double getDot2Decimal(int a, int b) {

        BigDecimal bigDecimal_1 = new BigDecimal(a);
        BigDecimal bigDecimal_2 = new BigDecimal(b);
        BigDecimal bigDecimal_result = bigDecimal_1.divide(bigDecimal_2, new MathContext(4));
        Double double1 = new Double(bigDecimal_result.toString());
        return double1;
    }


    public static File byte2File(byte[] buf, String filePath, String fileName){
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try{
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()){
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            if (bos != null){
                try{
                    bos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


}
