package com.zans.mms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.zans.base.exception.BusinessException;
import com.zans.base.util.DateHelper;
import com.zans.base.util.PdfHelper;
import com.zans.base.util.PdfUtils;
import com.zans.base.util.StringHelper;
import com.zans.mms.dao.BaseVfsDao;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.service.IPdfService;
import com.zans.mms.vo.ticket.TicketBaseMfRespVO;
import com.zans.mms.vo.ticket.TicketFlowRespVO;
import com.zans.mms.vo.ticket.TicketsDispatchPdfVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
* @Title: Pdf ServiceImpl
* @Description: pfd打印 Service
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/9/21
*/
@Service("pdfService")
@Slf4j
public class PdfServiceImpl implements IPdfService {

    @Autowired
    private BaseVfsDao baseVfsDao;

    @Value("${api.imgUrl.folder}")
    private String imgUrl;

    @Value("${api.export.folder}")
    String exportFolder;

    @Value("${api.upload.folder}")
    String uploadFolder;


    @Override
    public String generatePdfByTemplate(String templatePath, Object dataObj) {
        Map<String,Object> stringObjectMap= JSON.parseObject(JSON.toJSONString(dataObj),new TypeReference<Map<String,Object>>(){});
        Map<String,Object> o=new HashMap();
        o.put("dataMap",stringObjectMap);
        // 模板路径
//        String templatePath = "/home/release/file/upload/template/pdf/派工单.pdf";
        // 生成的新文件路径
        String newPDFPath = "/home/release/file/upload/template/testout1.pdf";
        PdfUtils.generatePdfByTemplate(o,templatePath,newPDFPath);

        return newPDFPath;
    }

    private void addPdfTableDispatchHeadRow(PdfPTable table , int columnSize, String... values){
        if (columnSize != values.length) {
            throw new BusinessException("PdfTableDispatchHeadRow  error: columnSize != values.length");
        }
        for (String value : values) {
            table.addCell(PdfHelper.createCell(value, PdfHelper.TEXT_FONT, Element.ALIGN_RIGHT, 1, false));
        }
    }

    private void addPdfTableRow(PdfPTable table , int columnSize, Font font, float height, String... values){
        if (columnSize != values.length) {
            throw new BusinessException("addPdfTableRow  error: columnSize != values.length");
        }
        for (String value : values) {
            table.addCell(PdfHelper.createCell(value,font, Element.ALIGN_CENTER,height));
        }
    }

    @Override
    public String generateDispatchPdf(TicketsDispatchPdfVO pdfVO) {
        String fileName =  pdfVO.getTicketCodeResult()+"派工单"+ pdfVO.getDispatchTime() + pdfVO.getPointName().replace("/","-")+".pdf";
        List<TicketFlowRespVO> ticketFlowList = pdfVO.getTicketFlowList();
        Document document = null;
        FileOutputStream fos = null;
        try {
            document = new Document(PageSize.A4);
            File file = new File(exportFolder +fileName);
            file.createNewFile();
            fos = new FileOutputStream(file);
            PdfWriter.getInstance(document, fos);
            document.open();
            pdfDesc(document, fileName);

            Paragraph title = new Paragraph("武汉市公安局交通管理局科技管理处委托单", PdfHelper.TITLE_FONT);
            ticketPdfTitle(title, 1, 12, 24);
            // 表格第0,1行  {50, 230, 50, 75, 75 }
            PdfPTable table = PdfHelper.createTable(new float[] {80, 200, 50, 150 });
            addPdfTableDispatchHeadRow(table,4,"","","  ","委托单号:"+pdfVO.getTicketCodeResult());
            addPdfTableRow(table,4,PdfHelper.TEXT_FONT,25f,"派工申请日期",pdfVO.getDispatchTime(),"派工来源",pdfVO.getIssueSource());
            // 表格第2行
            PdfPTable table2 = PdfHelper.createTable(new float[] {80, 110, 90, 50, 150 });
            addPdfTableRow(table2,5,PdfHelper.TEXT_FONT,25f,"运维单位",pdfVO.getOrgName(),pdfVO.getAreaName(),"施工类别",pdfVO.getDeviceTypeName());
            // 表格第3,4,5行
            PdfPTable table3 = PdfHelper.createTable(new float[] {80, 200, 50, 150 });
            setDispatchPointAmount(pdfVO, table3);
           /* PdfPTable table4 = PdfHelper.createTable(new float[] {80, 200, 50, 150 });
            addPdfTableRow(table4,4,PdfHelper.TEXT_FONT,25f,"工程内容","","","");*/
            List<TicketBaseMfRespVO> mfPdfList = pdfVO.getBaseMfPdfRespVOList();
            // 表格第6行
            PdfPTable table5 = PdfHelper.createTable(new float[] {480 });

            setContentOfEntrustment(pdfVO, table5,mfPdfList);



            PdfPTable projectTable = PdfHelper.createTable(new float[] {30,50, 200, 50, 75, 75 });
            int i =1;
            i = setDispatchProjectItems(mfPdfList, projectTable, i,pdfVO);
            PdfPTable table6 = PdfHelper.createTable(new float[] {240,240});

            if(ticketFlowList!=null && ticketFlowList.size()>1&&!StringUtils.isEmpty(ticketFlowList.get(1).getCreator())) {
                //table6.addCell(PdfHelper.createCell("监理单位意见："+ticketFlowList.get(1).getMsg().split("   ")[1]+"\n                                                  "+ticketFlowList.get(1).getCheckTime(),PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,75f));
                String comment = "同意";
                if (!StringUtils.isEmpty(ticketFlowList.get(1).getMsg())) {
                    String[] s = ticketFlowList.get(1).getMsg().split("   ");
                    if (s.length > 1) {
                        comment = s[1];
                    }
                }
                table6.addCell(PdfHelper.createCell("监理单位意见：" + comment, PdfHelper.TEXT_FONT, Element.ALIGN_TOP,Element.ALIGN_TOP,1, 75f));
            }else {
                table6.addCell(PdfHelper.createCell("监理单位意见：", PdfHelper.TEXT_FONT, Element.ALIGN_TOP,Element.ALIGN_TOP,1, 75f));
            }
            if(ticketFlowList!=null && ticketFlowList.size()>2&&!StringUtils.isEmpty(ticketFlowList.get(2).getCreator())){
                String comment = "同意";
                if (!StringUtils.isEmpty(ticketFlowList.get(2).getMsg())) {
                    String[] s = ticketFlowList.get(2).getMsg().split("   ");
                    if (s.length > 1) {
                        comment = s[1];
                    }
                }
                table6.addCell(PdfHelper.createCell("外场巡查队长意见: "+comment,PdfHelper.TEXT_FONT,Element.ALIGN_TOP,Element.ALIGN_TOP,1, 75f));
            }else{

                table6.addCell(PdfHelper.createCell("外场巡查队长意见:",PdfHelper.TEXT_FONT,Element.ALIGN_TOP,Element.ALIGN_TOP,1, 75f));
            }

            PdfPTable table7 = PdfHelper.createTable(new float[] {165, 165, 150});
            //处项目负责人意见
            setDispatchIdeaTwoWithCheck(table7,ticketFlowList);
            PdfPTable table8 = PdfHelper.createTable(new float[] {480});
            setDispatchIdeaThree(table8);

            document.add(title);
            document.add(table);
            document.add(table2);
            document.add(table3);
           // document.add(table4);
            document.add(table5);

            i = setDispatchProjectPadding(pdfVO, document, mfPdfList, projectTable, i);
            document.add(table6);
            document.add(table7);
            document.add(table8);
            String dispatchName="派工编号："+pdfVO.getTicketCodeResult();
            if (mfPdfList.size()>4||(null!=pdfVO.getIsMerge()&&pdfVO.getIsMerge()==1)){
                setDispatchProjectNewPage(pdfVO, document, projectTable, i,dispatchName);
            }
            /**
             * 添加派工单图片
             */
            List<BaseVfs> vfsList = baseVfsDao.getDispatchAdjunctList(pdfVO.getId());

            addDispatchPdfImage(document, vfsList,mfPdfList,dispatchName,pdfVO.getIsMerge());
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closePdf(document, fos);
        }
        return fileName;
    }

    /**
     * 派工单项目列表
     * @param mfPdfList
     * @param projectTable
     * @param i
     * @return
     */
    private int setDispatchProjectItems(List<TicketBaseMfRespVO> mfPdfList, PdfPTable projectTable, int i,TicketsDispatchPdfVO pdfVO) {
        addPdfTableRow(projectTable,6, PdfHelper.KEY_FONT,25f,"序号","项目编号","项目名称","数量","综合单价","预算金额");
        for (TicketBaseMfRespVO vo : mfPdfList) {
            String amount=String.valueOf(vo.getAmount());
            for(int j=0;j<2;j++){
                if(amount.substring(amount.length()-1).equals("0")){
                    amount=amount.substring(0,amount.length()-1);
                }
            }
            if(amount.substring(amount.length()-1).equals(".")){
                amount=amount.substring(0,amount.length()-1);
            }
            addPdfTableRow(projectTable,6,PdfHelper.KEY_FONT,25f,String.valueOf(i++),vo.getDeviceCode(),
                    vo.getDeviceName(),amount,String.valueOf(vo.getDevicePrice()),String.valueOf(vo.getPredictPrice()));
        }
        return i;
    }

    /**
     * //处项目负责人意见
     * @param table7
     */
    private void setDispatchIdeaTwo(PdfPTable table7) {
        table7.addCell(PdfHelper.createCell("处项目负责人：\n",PdfHelper.TEXT_FONT, Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,2,70f));
        PdfPCell cellMerge = PdfHelper.createCell("\n\n是否紧急\n\n\n\n\n\n限定时间", PdfHelper.TEXT_FONT, Element.ALIGN_LEFT, 140f);
        cellMerge.setRowspan(2);
        cellMerge.setVerticalAlignment(Element.ALIGN_TOP);
        table7.addCell(cellMerge);
        table7.addCell(PdfHelper.createCell("处分管领导：\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,70f));
        table7.addCell(PdfHelper.createCell("处主管领导：\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,70f));
    }


    /**
     * //处项目负责人意见带意见
     * @param table7
     */
    private void setDispatchIdeaTwoWithCheck(PdfPTable table7,List<TicketFlowRespVO> ticketFlowList) {
        if(ticketFlowList!=null && ticketFlowList.size()>3&&!StringUtils.isEmpty(ticketFlowList.get(3).getCreator())) {
            String comment="同意";
            if(!StringUtils.isEmpty(ticketFlowList.get(3).getMsg())){
                String[] s = ticketFlowList.get(3).getMsg().split("   ");
                if(s.length>1){
                    comment=s[1];
                }
            }
            table7.addCell(PdfHelper.createCell("处项目负责人："+comment, PdfHelper.TEXT_FONT, Element.ALIGN_TOP,Element.ALIGN_TOP,2, 70f));
        }else{
            table7.addCell(PdfHelper.createCell("处项目负责人：\n", PdfHelper.TEXT_FONT, Element.ALIGN_TOP,Element.ALIGN_TOP,2, 70f));
        }
        PdfPCell cellMerge = PdfHelper.createCell("\n\n是否紧急\n\n\n\n\n\n限定时间", PdfHelper.TEXT_FONT, Element.ALIGN_LEFT, 140f);
        cellMerge.setRowspan(2);
        cellMerge.setVerticalAlignment(Element.ALIGN_TOP);
        table7.addCell(cellMerge);
        if(ticketFlowList!=null && ticketFlowList.size()>4&&!StringUtils.isEmpty(ticketFlowList.get(4).getCreator())) {
            String comment="同意";
            if(!StringUtils.isEmpty(ticketFlowList.get(4).getMsg())){
                String[] s = ticketFlowList.get(4).getMsg().split("   ");
                if(s.length>1){
                    comment=s[1];
                }
            }
            table7.addCell(PdfHelper.createCell("处分管领导："+comment+"\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_TOP,1,70f));
        }else{
            table7.addCell(PdfHelper.createCell("处分管领导：\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_TOP,1,70f));
        }

        if(ticketFlowList!=null && ticketFlowList.size()>5&&!StringUtils.isEmpty(ticketFlowList.get(5).getCreator())&&"吴钢".equals(ticketFlowList.get(5).getCreator())) {
            String comment="同意";
            if(!StringUtils.isEmpty(ticketFlowList.get(5).getMsg())){
                String[] s = ticketFlowList.get(5).getMsg().split("   ");
                if(s.length>1){
                    comment=s[1];
                }
            }
            table7.addCell(PdfHelper.createCell("处主管领导："+comment+"\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_TOP,1,70f));
        }else{
            table7.addCell(PdfHelper.createCell("处主管领导：\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_TOP,1,70f));

        }
        }

    /**
     * //局领导意见:
     * @param table8
     */
    private void setDispatchIdeaThree(PdfPTable table8) {
        table8.addCell(PdfHelper.createCell("局领导意见:\n\n〈总金额3万元（含3万元）以上的需由局领导审批〉",PdfHelper.TEXT_FONT, Element.ALIGN_LEFT,70f));
        table8.addCell(PdfHelper.createCell("派工反馈：",PdfHelper.HEAD_FONT,Element.ALIGN_LEFT,25f));
        PdfPCell tailCell = PdfHelper.createCell("工作要求：1.在规定时间完成任务；2.施工前请先联系大队；3.具体价格以审核结算价为准", PdfHelper.TAIL_FONT, Element.ALIGN_LEFT, 25f);
        tailCell.setBorder(0);
        tailCell.setPaddingTop(1.0f);
        tailCell.setPaddingBottom(1.0f);
        table8.addCell(tailCell);
    }

    private void setDispatchPointAmount(TicketsDispatchPdfVO pdfVO, PdfPTable table3) {
        /*if(!StringHelper.isEmpty(pdfVO.getPointName())&&pdfVO.getPointName().length()>=16){
            addPdfTableRow(table3,4, PdfHelper.KEY_FONT,25f,"工程所在路口",pdfVO.getPointName(),"责任人",pdfVO.getDutyContact());
        }else{
            addPdfTableRow(table3,4, PdfHelper.TEXT_FONT,25f,"工程所在路口",pdfVO.getPointName(),"责任人",pdfVO.getDutyContact());
        }*/
        addPdfTableRow(table3,4, PdfHelper.TEXT_FONT,25f,"工程所在路口","","责任人",pdfVO.getDutyContact());
        table3.addCell(PdfHelper.createCell("预算总金额",PdfHelper.TEXT_FONT, Element.ALIGN_CENTER,25f));
        table3.addCell(PdfHelper.createCell(pdfVO.getPredictCost()+"元",PdfHelper.TEXT_FONT, Element.ALIGN_CENTER,25f));
        table3.addCell(PdfHelper.createCell("维护单位\n签收人",PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        table3.addCell(PdfHelper.createCell(pdfVO.getApplyContact(),PdfHelper.TEXT_FONT, Element.ALIGN_CENTER,25f));
    }

    private void setContentOfEntrustment(TicketsDispatchPdfVO pdfVO, PdfPTable table4,List<TicketBaseMfRespVO> mfPdfList){
        PdfPCell projectTexRow1 = PdfHelper.createCell("工程内容：\n" , PdfHelper.TEXT_FONT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, 1, 20f);
        projectTexRow1.setBorderWidthBottom(0);
        float moreHeight=100-mfPdfList.size()*25f;
        if(moreHeight<0){
            moreHeight=100f;
        }
        if(null!=pdfVO.getIsMerge()){
            moreHeight=100f;
        }


        PdfPCell projectTexRow2 = PdfHelper.createCell(pdfVO.getRemark(), PdfHelper.KEY_FONT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, 1, 90f + moreHeight);
        projectTexRow2.setBorderWidthTop(0);
        table4.addCell(projectTexRow1);
        table4.addCell(projectTexRow2);


    }

    /**
     * 派工单项目清单另外加一页
     * @param pdfVO
     * @param document
     * @param projectTable
     * @param i
     * @throws DocumentException
     */
    private void setDispatchProjectNewPage(TicketsDispatchPdfVO pdfVO, Document document, PdfPTable projectTable, int i,String dispatchName) throws DocumentException {
        //另外加一页
        document.newPage();
        Paragraph title2 = new Paragraph(dispatchName+"  附件1：项目清单", PdfHelper.TEXT_FONT);
        ticketPdfTitle(title2, 0, 1, 12);
        document.add(title2);

        projectTotalRow(pdfVO, projectTable, i);
        document.add(projectTable);
    }

    /**
     * 派工单项目清单填充
     * @param pdfVO
     * @param document
     * @param mfPdfList
     * @param projectTable
     * @param i
     * @return
     * @throws DocumentException
     */
    private int setDispatchProjectPadding(TicketsDispatchPdfVO pdfVO, Document document, List<TicketBaseMfRespVO> mfPdfList, PdfPTable projectTable, int i) throws DocumentException {
        if (mfPdfList.size() < 5&&null==pdfVO.getIsMerge()){
            i = projectTotalRow(pdfVO, projectTable, i);
            document.add(projectTable);
        } else {
            PdfPTable table5 = PdfHelper.createTable(new float[] {480});
            table5.addCell(PdfHelper.createCell("项目清单见附页一",PdfHelper.TEXT_FONT, Element.ALIGN_LEFT,40f));
            document.add(table5);
        }
        return i;
    }

    private void ticketPdfTitle(Paragraph title, int alignment, int indentationLeft, int firstLineIndent) {
        title.setAlignment(alignment); //设置文字居中 0靠左   1，居中     2，靠右
        title.setIndentationLeft(indentationLeft); //设置左缩进
        title.setIndentationRight(12); //设置右缩进
        title.setFirstLineIndent(firstLineIndent); //设置首行缩进
        title.setLeading(20f); //行间距
        title.setSpacingBefore(5f); //设置段落上空白
        title.setSpacingAfter(10f); //设置段落下空白
    }

    private void pdfDesc(Document document, String title) {
        document.addTitle(title);// 标题
        document.addAuthor("beiming");// 作者
        document.addSubject("Subject@iText pdf sample");// 主题
        document.addKeywords("Keywords@iTextpdf");// 关键字
        document.addCreator("beiming");// 创建者
    }


    private int projectTotalRow(TicketsDispatchPdfVO pdfVO, PdfPTable projectTable, int i) {
        projectTable.addCell(PdfHelper.createCell(String.valueOf(i++),PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        projectTable.addCell(PdfHelper.createCell("合计",PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        projectTable.addCell(PdfHelper.createCell("",PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        projectTable.addCell(PdfHelper.createCell("",PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        projectTable.addCell(PdfHelper.createCell("",PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        projectTable.addCell(PdfHelper.createCell(pdfVO.getPredictCost(),PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        return i;
    }

    private int acceptProjectTotalRow(TicketsDispatchPdfVO pdfVO, PdfPTable projectTable, int i) {
        projectTable.addCell(PdfHelper.createCell(String.valueOf(i++),PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        projectTable.addCell(PdfHelper.createCell("",PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        projectTable.addCell(PdfHelper.createCell("总价",PdfHelper.KEY_FONT, Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,5,25f));
        projectTable.addCell(PdfHelper.createCell(pdfVO.getAcceptPredictCost(),PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        projectTable.addCell(PdfHelper.createCell(pdfVO.getAdjustCost(),PdfHelper.KEY_FONT, Element.ALIGN_CENTER,25f));
        projectTable.addCell(PdfHelper.createCell("",PdfHelper.KEY_FONT));
        return i;
    }


    @Override
    public String generateAcceptPdf(TicketsDispatchPdfVO pdfVO) {
        String fileName =  pdfVO.getTicketCodeResult()+"验收单"+ pdfVO.getAcceptTime() + pdfVO.getPointName().replace("/","-")+".pdf";
        Document document = null;
        FileOutputStream fos = null;
        List<TicketFlowRespVO> ticketFlowList = pdfVO.getTicketFlowList();
        try {
            document = new Document(PageSize.A4);
            File file = new File(exportFolder +fileName);
            file.createNewFile();
            fos = new FileOutputStream(file);
            PdfWriter.getInstance(document,fos);
            document.open();
            pdfDesc(document, fileName);

            Paragraph title = new Paragraph("武汉市公安局交通管理局科技管理处验收单", PdfHelper.TITLE_FONT);
            ticketPdfTitle(title, 1, 12, 24);
            // 表格第一行  {50, 230, 50, 75, 75 }
            PdfPTable table = PdfHelper.createTable(new float[] {80, 200, 50, 150 });
            addPdfTableDispatchHeadRow(table,4,"","","","派工单号:"+pdfVO.getTicketCodeResult());
            addPdfTableRow(table,4,PdfHelper.TEXT_FONT,25f,"验收日期",pdfVO.getAcceptTime(),"施工类别",pdfVO.getDeviceTypeName());
            addPdfTableRow(table,4,PdfHelper.TEXT_FONT,25f,"运维单位",pdfVO.getOrgName(),"工程验收金额",pdfVO.getAdjustCost()+"元" );

            List<TicketBaseMfRespVO> mfPdfList = pdfVO.getBaseMfPdfRespVOList();
            // 表格第7行 项目list
            PdfPTable projectTable = PdfHelper.createTable(new float[] {20, 40, 90, 55, 30, 30, 45, 45, 45, 35 });
            StringBuffer sbText = new StringBuffer(pdfVO.getTicketCodeResult());
            sbText.append(" ").append(pdfVO.getPointName()).append(":").append("新增").append(pdfVO.getDeviceTypeName()).append(",");

            int i =1;
            i = setAcceptProjectItems(mfPdfList, projectTable, sbText, i,pdfVO);
            String substring = sbText.substring(0, sbText.length() - 1)+"。";
            // 表格第3,4,5行
            PdfPTable table3 = PdfHelper.createTable(new float[] {480});
            setAcceptProjectContext(pdfVO.getAcceptanceInstructions(), table3,mfPdfList,pdfVO.getIsMerge());

            PdfPTable table8 = PdfHelper.createTable(new float[] {85,350});
            setAcceptSignOfLeaderWithCheck(table8,ticketFlowList);
            PdfPTable table9 = PdfHelper.createTable(new float[] {85,175,175});
            table9.addCell(PdfHelper.createCell("  业主单位(盖章) \n \n  签          字\n \n  日          期",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
            if(null!=ticketFlowList&&ticketFlowList.size()>3&&!StringUtils.isEmpty(ticketFlowList.get(3).getCreator())) {
                String comment="同意";
                if(!StringUtils.isEmpty(ticketFlowList.get(3).getMsg())){
                    String[] s = ticketFlowList.get(3).getMsg().split("   ");
                    if(s.length>1){
                        comment=s[1];
                    }
                }
                //table8.addCell(PdfHelper.createCell(comment+"\n\n", PdfHelper.TEXT_FONT));
                table9.addCell(PdfHelper.createCell(comment+"\n\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_LEFT));
                if(!StringUtils.isEmpty(ticketFlowList.get(2).getMsg())){
                    String[] s = ticketFlowList.get(2).getMsg().split("   ");
                    if(s.length>1){
                        comment=s[1];
                    }
                }
                table9.addCell(PdfHelper.createCell(comment+"\n\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_LEFT));

            }else if(null!=ticketFlowList&&ticketFlowList.size()>2&&!StringUtils.isEmpty(ticketFlowList.get(2).getCreator())){
                String comment="同意";
                if(!StringUtils.isEmpty(ticketFlowList.get(2).getMsg())){
                    String[] s = ticketFlowList.get(2).getMsg().split("   ");
                    if(s.length>1){
                        comment=s[1];
                    }
                }
                //table8.addCell(PdfHelper.createCell(comment+"\n\n", PdfHelper.TEXT_FONT));
                table9.addCell(PdfHelper.createCell("",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_LEFT));
                table9.addCell(PdfHelper.createCell(comment+"\n\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_LEFT));

            } else {
                table9.addCell(PdfHelper.createCell("", PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_LEFT));
                table9.addCell(PdfHelper.createCell("", PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_LEFT));
            }
            document.add(title);
            document.add(table);
            document.add(table3);
            i = setAcceptProjectPadding(pdfVO, document, mfPdfList, projectTable, i);
            document.add(table8);
            document.add(table9);
            String acceptName="派工编号:"+pdfVO.getTicketCodeResult();
            if (mfPdfList.size()>3||(null!=pdfVO.getIsMerge()&&pdfVO.getIsMerge()==1)){
                setAcceptProjectNewPage(pdfVO, document, projectTable, i,acceptName);
            }
            List<BaseVfs> vfsList = baseVfsDao.getAcceptAdjunctList(pdfVO.getId());
            addAcceptPdfImage(document, vfsList,mfPdfList.size(),acceptName,pdfVO.getIsMerge());

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closePdf(document, fos);
        }
        return fileName;
    }

    /**
     * 设置项目列表
     * @param mfPdfList
     * @param projectTable
     * @param sbText
     * @param i
     * @return
     */
    private int setAcceptProjectItems(List<TicketBaseMfRespVO> mfPdfList, PdfPTable projectTable, StringBuffer sbText, int i,TicketsDispatchPdfVO pdfVO) {
        addPdfTableRow(projectTable,10, PdfHelper.KEY_FONT,50f,"序号","项目编号","名称","型号","数量","单位","单价","预算金额","结算金额","监理确认量");
        for (TicketBaseMfRespVO vo : mfPdfList) {
            String amount=String.valueOf(vo.getAmount());
            String adjustAmount=String.valueOf(vo.getAdjAmount());
            for(int j=0;j<2;j++){
                if(amount.substring(amount.length()-1).equals("0")){
                    amount=amount.substring(0,amount.length()-1);
                }
                if(adjustAmount.substring(adjustAmount.length()-1).equals("0")){
                    adjustAmount=adjustAmount.substring(0,adjustAmount.length()-1);
                }
            }
            if(amount.substring(amount.length()-1).equals(".")){
                amount=amount.substring(0,amount.length()-1);
            }
            if(adjustAmount.substring(adjustAmount.length()-1).equals(".")){
                adjustAmount=adjustAmount.substring(0,adjustAmount.length()-1);
            }
            addPdfTableRow(projectTable,10,PdfHelper.KEY_FONT,50f,String.valueOf(i++),vo.getDeviceCode(),vo.getDeviceName(),vo.getDeviceModel(),
                    amount,String.valueOf(vo.getDeviceSpec()),String.valueOf(vo.getDevicePrice()),String.valueOf(vo.getPredictPrice()),
                    String.valueOf(vo.getAdjustPrice()),adjustAmount);
            sbText.append(vo.getDeviceName()).append(vo.getAmount()).append(vo.getDeviceSpec()).append(",");
        }
       /* addPdfTableRow(projectTable,10,PdfHelper.KEY_FONT,50f,String.valueOf(i++),"","总价","",
              "","","",String.valueOf(pdfVO.getPredictCost()),
                String.valueOf(pdfVO.getAdjustCost()),"");*/
        return i;
    }

    /**
     * 委托内容
     * @param substring
     * @param table3
     */
    private void setAcceptProjectContext(String substring, PdfPTable table3,List<TicketBaseMfRespVO> mfPdfList,Integer isMerge) {
        //PdfPCell projectTexRow1 = PdfHelper.createCell("委托内容：\n        \n\n" , PdfHelper.TEXT_FONT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, 1, 40f);
        //PdfPCell projectTexRow1 = PdfHelper.createCell("委托内容：", PdfHelper.TEXT_FONT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, 1, 20f);
        PdfPCell projectTexRow1 = PdfHelper.createCell("委托内容：\n" , PdfHelper.TEXT_FONT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, 1, 20f);
        projectTexRow1.setBorderWidthBottom(0);
        float moreHeight=150-mfPdfList.size()*50f;
        if(moreHeight<0f){
            moreHeight=150f;
        }
        if(null!=isMerge){
            moreHeight=150f;
        }
        //PdfPCell projectTexRow2 = PdfHelper.createCell("        "+substring, PdfHelper.KEY_FONT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, 1, 120f+moreHeight);
        PdfPCell projectTexRow2 = PdfHelper.createCell(substring, PdfHelper.KEY_FONT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, 1, 120f+moreHeight);
        projectTexRow2.setBorderWidthTop(0);


        table3.addCell(projectTexRow1);
        table3.addCell(projectTexRow2);
    }

    /**
     * 领导签字
     * @param table8
     */
    private void setAcceptSignOfLeader(PdfPTable table8) {
        table8.addCell(PdfHelper.createCell("  维护单位(盖章) \n \n  签          字\n \n  日          期",PdfHelper.TEXT_FONT, Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
        table8.addCell(PdfHelper.createCell("", PdfHelper.TEXT_FONT));
        table8.addCell(PdfHelper.createCell("  监理单位(盖章) \n \n  签          字\n \n  日          期",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
        table8.addCell(PdfHelper.createCell("",PdfHelper.TEXT_FONT));
        table8.addCell(PdfHelper.createCell("  业主单位(盖章) \n \n  签          字\n \n  日          期",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
        table8.addCell(PdfHelper.createCell("", PdfHelper.TEXT_FONT));
    }

    /**
     * 领导签字带审批意见
     * @param table8
     */
    private void setAcceptSignOfLeaderWithCheck(PdfPTable table8,List<TicketFlowRespVO> ticketFlowList) {
        table8.addCell(PdfHelper.createCell("  维护单位(盖章) \n \n  签          字\n \n  日          期",PdfHelper.TEXT_FONT, Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
        table8.addCell(PdfHelper.createCell("", PdfHelper.TEXT_FONT));
        table8.addCell(PdfHelper.createCell("  监理单位(盖章) \n \n  签          字\n \n  日          期",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
        if(null!=ticketFlowList&&ticketFlowList.size()>1&&!StringUtils.isEmpty(ticketFlowList.get(1).getCreator())){
            String comment="同意";
            if(!StringUtils.isEmpty(ticketFlowList.get(1).getMsg())){
                String[] s = ticketFlowList.get(1).getMsg().split("   ");
                if(s.length>1){
                    comment=s[1];
                }
            }
            //table8.addCell(PdfHelper.createCell(comment+"\n\n",PdfHelper.TEXT_FONT));
            table8.addCell(PdfHelper.createCell(comment+"\n\n",       PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_LEFT));
        }else{
            table8.addCell(PdfHelper.createCell("",PdfHelper.TEXT_FONT));
        }
      /*  table8.addCell(PdfHelper.createCell("  业主单位(盖章) \n \n  签          字\n \n  日          期",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
        if(null!=ticketFlowList&&ticketFlowList.size()>3&&!StringUtils.isEmpty(ticketFlowList.get(3).getCreator())) {
            String comment="同意";
            if(!StringUtils.isEmpty(ticketFlowList.get(3).getMsg())){
                String[] s = ticketFlowList.get(3).getMsg().split("   ");
                if(s.length>1){
                    comment=s[1];
                }
            }
            //table8.addCell(PdfHelper.createCell(comment+"\n\n", PdfHelper.TEXT_FONT));
            table8.addCell(PdfHelper.createCell(comment+"\n\n",PdfHelper.TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_LEFT));
        }else{
            table8.addCell(PdfHelper.createCell("", PdfHelper.TEXT_FONT));
            //table8.addCell(PdfHelper.createCell("", PdfHelper.TEXT_FONT));
        }*/
    }


    /**
     * 设置项目列表填充
     * @param pdfVO
     * @param document
     * @param mfPdfList
     * @param projectTable
     * @param i
     * @return
     * @throws DocumentException
     */
    private int setAcceptProjectPadding(TicketsDispatchPdfVO pdfVO, Document document, List<TicketBaseMfRespVO> mfPdfList, PdfPTable projectTable, int i) throws DocumentException {
        if (mfPdfList.size() < 4&&pdfVO.getIsMerge()==null){
            i = acceptProjectTotalRow(pdfVO, projectTable, i);
            document.add(projectTable);
        } else {
            PdfPTable table5 = PdfHelper.createTable(new float[] {480});
            table5.addCell(PdfHelper.createCell("项目清单见附页一",PdfHelper.TEXT_FONT, Element.ALIGN_LEFT,40f));
            document.add(table5);
        }
        return i;
    }

    /**
     * //另外加一页 附件1：项目清单
     * @param pdfVO
     * @param document
     * @param projectTable
     * @param i
     * @throws DocumentException
     */
    private void setAcceptProjectNewPage(TicketsDispatchPdfVO pdfVO, Document document, PdfPTable projectTable, int i,String acceptName) throws DocumentException {
        document.newPage();
        Paragraph title2 = new Paragraph(acceptName+"  附件1：项目清单", PdfHelper.TEXT_FONT);
        ticketPdfTitle(title2, 0, 1, 12);
        document.add(title2);

        acceptProjectTotalRow(pdfVO, projectTable, i);
        document.add(projectTable);
    }

    private void addAcceptPdfImage(Document document, List<BaseVfs> vfsList,int size,String acceptName,Integer isMerge) throws DocumentException, IOException {
        if (vfsList.size()>0) {
            //另外加一页 附件2：图片
            document.newPage();
            String name=acceptName+"  附件2：图片";
            if(size<=3&&null==isMerge){
                 name=acceptName+"  附件1：图片";
            }
            Paragraph title2 = new Paragraph(name, PdfHelper.TEXT_FONT);
            ticketPdfTitle(title2, 0, 1, 12);
            document.add(title2);

            List<BaseVfs> vfs = vfsList.stream().limit(50).collect(Collectors.toList());

            PdfPTable tableImages = PdfHelper.createTable(new float[] {80f,80f,80f,80f,80f,80f});
            for (BaseVfs vf : vfs) {
//                Image image = Image.getInstance("/home/nathan/tools/图片1.png");
                Image image = Image.getInstance(imgUrl+vf.getRawFilePath());
                image.scaleAbsolute(10f,120f);
                image.scalePercent(80,120);
                tableImages.addCell(PdfHelper.createCellImage(image,120f));
            }
            int vSize = vfs.size();
            int mSize = vSize % 6;
            for (int j = 0; j < 6-mSize; j++) {
                tableImages.addCell(PdfHelper.createCell("",PdfHelper.HEAD_FONT,1,1,false));
            }
            document.add(tableImages);
        }
    }

    private void addDispatchPdfImage(Document document, List<BaseVfs> vfsList,List<TicketBaseMfRespVO> mfPdfList,String dispatchName,Integer isMerge) throws DocumentException, IOException {
        if (vfsList.size()>0) {
            //另外加一页 附件2：图片
            document.newPage();
            String name = dispatchName+"  附件1：图片";
            if (mfPdfList.size() > 4||isMerge!=null){
                name=dispatchName+"  附件2：图片";
            }
            Paragraph title2 = new Paragraph(name, PdfHelper.TEXT_FONT);
            ticketPdfTitle(title2, 0, 1, 12);
            document.add(title2);

            List<BaseVfs> vfs = vfsList.stream().limit(50).collect(Collectors.toList());

            PdfPTable tableImages = PdfHelper.createTable(new float[] {80f,80f,80f,80f,80f,80f});
            for (BaseVfs vf : vfs) {
//                Image image = Image.getInstance("/home/nathan/tools/图片1.png");
                Image image = Image.getInstance(imgUrl+vf.getRawFilePath());
                image.scaleAbsolute(10f,120f);
                image.scalePercent(80,120);
                tableImages.addCell(PdfHelper.createCellImage(image,120f));
            }
            int vSize = vfs.size();
            int mSize = vSize % 6;
            for (int j = 0; j < 6-mSize; j++) {
                tableImages.addCell(PdfHelper.createCell("",PdfHelper.HEAD_FONT,1,1,false));
            }
            document.add(tableImages);
        }
    }

    private void closePdf(Document document, FileOutputStream fos) {
        if (document != null){
            try{
                document.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (fos != null){
            try{
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}