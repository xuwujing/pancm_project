package com.pancm.test.qrcode;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码生成和读的工具类
 */
public class QrCodeCreateUtil {

    /**
     * 生成包含字符串信息的二维码图片
     *
     * @param outputStream 文件输出流路径
     * @param content      二维码携带信息
     * @param qrCodeSize   二维码图片大小
     * @param imageFormat  二维码的格式
     * @return the boolean
     * @throws WriterException the writer exception
     * @throws IOException     the io exception
     */
    public static boolean createQrCode(OutputStream outputStream, String content, int qrCodeSize, String imageFormat) throws WriterException, IOException {
        //设置二维码纠错级别ＭＡＰ
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  // 矫错级别
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //创建比特矩阵(位矩阵)的QR码编码的字符串
        BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
        // 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth - 200, matrixWidth - 200, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // 使用比特矩阵画并保存图像
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i - 100, j - 100, 1, 1);
                }
            }
        }
        return ImageIO.write(image, imageFormat, outputStream);
    }

    /**
     * 读二维码并输出携带的信息
     *
     * @param inputStream the input stream
     * @throws IOException the io exception
     */
    public static void readQrCode(InputStream inputStream) throws IOException {
        //从输入流中获取字符串信息
        BufferedImage image = ImageIO.read(inputStream);
        //将图像转换为二进制位图源
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result = null;
        try {
            result = reader.decode(bitmap);
        } catch (ReaderException e) {
            e.printStackTrace();
        }
        assert result != null;
        System.out.println(result.getText());
    }


    /**
     * 解析图片中的二维码文本信息
     *
     * @param imagePath 含二维码图片的路径
     * @param cachePath 缓存目录路径
     * @return 解析出的二维码文本信息
     */
    public static String decodeQRInfoFromImage(String imagePath, String cachePath) {
        String qrInfo = null;
        try {
            //判断图片路径是否为空
            if (imagePath == null || imagePath.trim().isEmpty()) {
                System.err.println("Parameter \'imagePath\' cannot be empty");
                return null;
            }

            //判断图片文件是否存在
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.err.println("The image file is not exits");
                return null;
            }

            //判断是否为图片
            try {
                Image image = ImageIO.read(imageFile);
                if (image == null) {
                    System.err.println("The image file is not real picture");
                    return null;
                }
            } catch (IOException ex) {
                System.err.println("The image file is not real picture");
                return null;
            }

            //判断缓存目录是否为空
            if (cachePath == null || cachePath.trim().isEmpty()) {
                System.err.println("Parameter \'cachePath\' cannot be empty");
                return null;
            }

            //判断缓存目录是否存在
            File cacheFile = new File(cachePath);
            if (!cacheFile.exists() || !cacheFile.isDirectory()) {
                System.err.println("cachePath is not exits or is not directory");
                return null;
            }

            ImageIO.setCacheDirectory(new File(cachePath));
            BufferedImage image = ImageIO.read(new File(imagePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            qrInfo = result.getText();
            return qrInfo;
        } catch (Exception e) {
            return null;
        }

    }


    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }
    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
     * 测试代码
     *
     * @param args the input arguments
     * @throws IOException     the io exception
     * @throws WriterException the writer exception
     */
    public static void main(String[] args) throws IOException, WriterException {
//       test1();
//       test2();
       test3();
    }


    private static void test1() throws IOException, WriterException {
        createQrCode(Files.newOutputStream(new File("d:\\pancm.jpg").toPath()), "http://www.panchengming.com", 900, "JPEG");
        readQrCode(Files.newInputStream(new File("d:\\pancm.jpg").toPath()));
    }

    private static void test2() throws IOException, WriterException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("1","a");
        jsonObject.put("2","b");
        jsonObject.put("3","c");
        String filePath = "d:\\test\\test.jpg";
//        String filePath = "d:\\test\\Lark20210428-165847.jpeg";
        createQrCode(Files.newOutputStream(new File(filePath).toPath()), jsonObject.toJSONString(), 900, "JPEG");
        readQrCode(Files.newInputStream(new File(filePath).toPath()));
    }

    private static void test3() throws IOException, WriterException {
        String filePath = "d:\\test\\test.jpg";
//        String filePath = "d:\\test\\Lark20210428-165847.jpeg";
        String cachePath = "d:\\test";
        System.out.println(decodeQRInfoFromImage(filePath,cachePath));
    }

}
