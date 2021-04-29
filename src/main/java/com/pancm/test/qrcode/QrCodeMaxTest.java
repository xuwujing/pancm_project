package com.pancm.test.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Hashtable;

public class QrCodeMaxTest {
    public static void main(String[] args) {
        String filePath = "d:/qr_png.png";
        String str = "";
                /*
                for (int i = 0; i < 2685; i++) {
                        str += 1;
                }
                */

        for (int i = 0; i < 635; i++) {
            str += "潘";
        }

        encode(str, filePath);
        decode(filePath);
    }

    // qrcode 编码
    static void encode(String conent, String filePath) {
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder encoder = charset.newEncoder();
        byte[] b = null;
        try { // Convert a string to ISO-8859-1 bytes in a ByteBuffer
            System.out.println("-------->" + conent.length());
            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(conent));
            b = bbuf.array();
        } catch (CharacterCodingException e) {
            System.out.println(e.getMessage());
        }
        String data = "";
        try {
            data = new String(b, "iso8859-1");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } // get a byte matrix for the data
        BitMatrix matrix = null;
        int h = 900;
        int w = 800;
        Writer writer = new QRCodeWriter();
        try {
            matrix = writer.encode(data,
                    BarcodeFormat.QR_CODE, w, h);
        } catch (WriterException e) {
            System.out.println(e.getMessage());
        }
        File file = new File(filePath);
        try {
            MatrixToImageWriter.writeToPath(matrix, "PNG", file.toPath());
            System.out.println("printing to " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // qrcode 解码
    static void decode(String file) {
        try {
            Result result = null;
            BufferedImage image = null;
            image = ImageIO.read(new File(file));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            result = new MultiFormatReader().decode(bitmap, hints);
            String rtn = result.getText();
            System.out.println(rtn);
            System.out.println(rtn.length());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
