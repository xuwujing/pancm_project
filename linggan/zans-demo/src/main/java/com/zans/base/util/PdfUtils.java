package com.zans.base.util;

import java.util.Map;
//import com.zans.mms.vo.tickets.PdfTicketsVO;


public class PdfUtils {
    // 利用模板生成pdf  可能有用
    public static void generatePdfByTemplate(Map<String,Object> o,String templatePath,String newPDFPath) {

// /       PdfReader reader;
// /       FileOutputStream out;
// /       ByteArrayOutputStream bos;
// /       PdfStamper stamper;
// /       try {
// /           BaseFont bf = BaseFont.createFont("/home/release/file/upload/template/pdf/simsun.ttc,1" , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
// /           Font FontChinese = new Font(bf, 5, Font.NORMAL);
// /           out = new FileOutputStream(newPDFPath);// 输出流
// /           reader = new PdfReader(templatePath);// 读取pdf模板
// /           bos = new ByteArrayOutputStream();
// /           stamper = new PdfStamper(reader, bos);
// /           AcroFields form = stamper.getAcroFields();
// /           //文字类的内容处理
// /           Map<String,String> dataMap = (Map<String,String>)o.get("dataMap");
// /           form.addSubstitutionFont(bf);
// /           for(String key : dataMap.keySet()){
// /               String value = dataMap.get(key);
// /               form.setField(key,value);
// /           }
// /           // 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
// /           stamper.setFormFlattening(true);
// /           stamper.close();
// /           Document doc = new Document();
// /           Font font = new Font(bf, 32);
// /           PdfCopy copy = new PdfCopy(doc, out);
// /           doc.open();
// /           PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
// /           copy.addPage(importPage);
// /           doc.close();
///
// /       } catch (IOException e) {
// /           e.printStackTrace();
// /           System.out.println(e);
// /       } catch (DocumentException e) {
// /           e.printStackTrace();
// /           System.out.println(e);
// /       }

    }

    public static void main(String[] args) {

//        PdfTicketsVO vo = new PdfTicketsVO();
//        vo.setTicketsNum("BSYJA20210204003");
//        vo.setHappenDate("2021/2/4");
//        vo.setSource("巡查");
//        vo.setConstructionUnit("毕昇云-2020");
//        vo.setAreaName("江岸区");
//        vo.setProjectType("诱导屏");
//        vo.setPointName("工程所在路口");
//        vo.setBelonger("徐凯");
//        vo.setAmount("2000");
//        vo.setMaintainPerson("熊锐");
//        vo.setProjectContent("BSYJA20210204003吉庆街停车场，停车屏卡座故障，需更换1张接收卡。BSYJA20210204003吉庆街停车场");
//        vo.setSupervisorIdea("监理单位意见");
//        vo.setBureauBelonger("BureauBelonger");
//        vo.setEndTime("EndTime");
//        vo.setRunNow("是");
//        vo.setBureau1Idea("处分管领导");
//        vo.setBureau2Idea("处主管领导");
//        vo.setBureau3Idea("局领导意见");
//        vo.setDispatchFeedback("派工反馈");
//
//        Map<String,Object> stringObjectMap= JSON.parseObject(JSON.toJSONString(vo),new TypeReference<Map<String,Object>>(){});
//        System.out.println(stringObjectMap);
//        Map<String,Object> o=new HashMap();
//        o.put("dataMap",stringObjectMap);
//        // 模板路径
//        String templatePath = "/home/release/file/upload/template/pdf/派工单.pdf";
//        // 生成的新文件路径
//        String newPDFPath = "/home/release/file/upload/template/testout1.pdf";
//        pdfout(o,templatePath,newPDFPath);

    }
}