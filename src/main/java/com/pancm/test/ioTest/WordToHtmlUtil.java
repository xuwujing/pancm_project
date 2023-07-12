package com.pancm.test.ioTest;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * @date
 * @description
 */
public class WordToHtmlUtil {

    private static final Logger logger = LoggerFactory.getLogger(WordToHtmlUtil.class);

    /**
     * 上传Word文档，返回解析后的Html
     */
    public static String docToHtmlText(MultipartFile file) throws Exception {
        //使用字符数组流获取解析的内容
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream outStream = new BufferedOutputStream(baos);
        try {
            //将上传的文件传入Document转换
            HWPFDocument wordDocument = new HWPFDocument(file.getInputStream());
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
            //将读取到的图片上传并添加链接地址
            wordToHtmlConverter.setPicturesManager((imageStream, pictureType, name, width, height) -> {
                try {
                    //首先要判断图片是否能识别
                    if (pictureType.equals(PictureType.UNKNOWN)) {
                        return "[不能识别的图片]";
                    }
                    //此处上传到自己的文件服务器 todo
                    String qiNiuName = "";//文件名
//                    boolean upload = FileUtil.upload(new        FileInputStream(fileImage), qiNiuName);
                    return "上传后的图片地址";

                } catch (Exception e) {
                    logger.info("upload exception", e);
                }
                return "[图片上传失败]";
            });
            // word文档转Html文档
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(outStream);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            String content = baos.toString();
            logger.info("docToHtmlText--->{}", content);

            return content;
        } catch (Exception e) {
            logger.error("docToHtmlText 异常", e);
//            throw new AppRuntimeException(e);
        } finally {
            baos.close();
            outStream.close();
        }
        return null;
    }

    /**
     * 上传docx文档，返回解析后的Html
     */
    public static String docxToHtmlText(MultipartFile file) throws Exception {
        ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
        try {
            // 将上传的文件传入Document转换
            XWPFDocument docxDocument = new XWPFDocument(file.getInputStream());
            XHTMLOptions options = XHTMLOptions.create();
            // 设置图片存储路径
            String path = System.getProperty("java.io.tmpdir");
            String firstImagePathStr = path + "/" + System.currentTimeMillis();
            options.setExtractor(new FileImageExtractor(new File(firstImagePathStr)));
            options.URIResolver(new BasicURIResolver(firstImagePathStr));
            // 转换html
            docxDocument.createNumbering();
            XHTMLConverter.getInstance().convert(docxDocument, htmlStream, options);
            String htmlStr = htmlStream.toString();

            String middleImageDirStr = "/word/media";
            String imageDirStr = firstImagePathStr + middleImageDirStr;
            File imageDir = new File(imageDirStr);
            String[] imageList = imageDir.list();
            if (imageList != null) {
                for (int i = 0; i < imageList.length; i++) {
                    try {
                        String oneImagePathStr = imageDirStr + "/" + imageList[i];
                        File fileImage = new File(oneImagePathStr);
                        if (fileImage.exists()) {
                            String name = fileImage.getName();
                            String suffix = name.substring(name.indexOf("."), name.length()).toLowerCase();
                            //此处上传到自己的文件服务器 todo
                            String qiNiuName = "";//文件名
//                              boolean upload = FileUtil.upload(new FileInputStream(fileImage), qiNiuName);
//                            if (!upload) {
//                                continue;
//                            } else {
//                                //修改文档中的图片信息
//                                htmlStr = htmlStr.replace(oneImagePathStr, "上传后的图片地址");
//                            }
                        }
                    } catch (Exception e) {
                        logger.info("upload docxToHtmlText exception", e);
                    }
                }
            }
            //删除图片路径
            File firstImagePath = new File(firstImagePathStr);
            FileUtils.deleteDirectory(firstImagePath);
            return htmlStr;
        } catch (Exception e) {
            logger.error("docxToHtmlText 解析异常", e);
//            throw new RuntimeException(e);
        } finally {
            if (htmlStream != null) {
                htmlStream.close();
            }
        }
        return null;
    }


    public static void main(String[] args) {
        try {
//            test1();
//            String filePath = "D:\\pdf\\f3.docx";
            String filePath = "D:\\pdf\\Legal-Contract Temp-HR Admin-51.docx";
            String content = docxToHtmlText(getMulFileByPath(filePath));
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void test1() throws Exception {

        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        String imagePathStr = path.getAbsolutePath() + "\\static\\image\\";
//        String filePath = "D:\\pdf\\Legal-Contract Temp-HR Admin-51. 维修工程合同.docx";
        String sourceFileName = "D:\\pdf\\f3.docx";
        String targetFileName = "test2.html";
        File file = new File(imagePathStr);
        if (!file.exists()) {
            file.mkdirs();
        }
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
        //保存图片，并返回图片的相对路径
        wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
            try (FileOutputStream out = new FileOutputStream(imagePathStr + name)) {
                out.write(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "image/" + name;
        });
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(new File(targetFileName));
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        System.out.println("===");

    }


    /**
     * 获取MultipartFile文件
     *
     * @param picPath
     * @return
     */
    private static MultipartFile getMulFileByPath(String picPath) {
        FileItem fileItem = createFileItem(picPath);
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        return mfile;
    }

    private static FileItem createFileItem(String filePath) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "MyFileName" + extFile);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

}
