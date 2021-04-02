package com.pancm.test.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

public class PdfHelper {

    // main测试
    public static void main(String[] args) throws Exception {
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象

            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File("PDFDemo.pdf");
            file.createNewFile();
            PdfWriter.getInstance(document, new FileOutputStream(file));

            // 3.打开文档
            document.open();
			document.addTitle("Title@PDF-Java");// 标题
			document.addAuthor("Author@umiz");// 作者
			document.addSubject("Subject@iText pdf sample");// 主题
			document.addKeywords("Keywords@iTextpdf");// 关键字
			document.addCreator("Creator@umiz`s");// 创建者

            // 4.向文档中添加内容
            new PdfHelper().generatePDF(document);

            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 定义全局的字体静态变量
	public static Font TITLE_FONT;
	public static Font HEAD_FONT;
	public static Font TAIL_FONT;
	public static Font KEY_FONT;
	public static Font TEXT_FONT;
    // 最大宽度
	public static int maxWidth = 520;
	// 静态代码块
    static {
        try {
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
//            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            BaseFont bfChinese = BaseFont.createFont(""+ "/template/pdf/simsun.ttc,1" , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            TITLE_FONT = new Font(bfChinese, 18, Font.BOLD);
            HEAD_FONT = new Font(bfChinese, 14, Font.NORMAL);
            KEY_FONT = new Font(bfChinese, 10, Font.NORMAL);
            TAIL_FONT = new Font(bfChinese, 11, Font.NORMAL);
            TEXT_FONT = new Font(bfChinese, 12, Font.NORMAL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 生成PDF文件
	public void generatePDF(Document document) throws Exception {
    	// 段落
		Paragraph title = new Paragraph("武汉市公安局交通管理局科技管理处工程委托单", TITLE_FONT);
		title.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
		title.setIndentationLeft(12); //设置左缩进
		title.setIndentationRight(12); //设置右缩进
		title.setFirstLineIndent(24); //设置首行缩进
		title.setLeading(20f); //行间距
		title.setSpacingBefore(5f); //设置段落上空白
		title.setSpacingAfter(10f); //设置段落下空白




		// 表格
		PdfPTable table = createTable(new float[] {90, 150, 90, 150 });
		table.addCell(createCell("派工单号  BSYJH20201113001", TEXT_FONT, Element.ALIGN_RIGHT, 4, false));

		table.addCell(createCell("验收日期", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table.addCell(createCell("2020/12/5", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table.addCell(createCell("工程类别", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table.addCell(createCell("信号灯", TEXT_FONT, Element.ALIGN_CENTER,25f));

		table.addCell(createCell("施工单位", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table.addCell(createCell("毕昇云", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table.addCell(createCell("工程验收金额", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table.addCell(createCell("8600.00 元", TEXT_FONT, Element.ALIGN_CENTER,25f));

		table.addCell(createCell("    工程内容：", TEXT_FONT, Element.ALIGN_LEFT,Element.ALIGN_LEFT,4,140f));

		PdfPTable table2 = createTable(new float[] {30, 55, 55, 55, 30, 30, 45, 45, 45, 45 });
		table2.addCell(createCell("序号", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table2.addCell(createCell("项目编号", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table2.addCell(createCell("名称", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table2.addCell(createCell("型号", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table2.addCell(createCell("数量", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table2.addCell(createCell("单位", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table2.addCell(createCell("单价", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table2.addCell(createCell("预算金额", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table2.addCell(createCell("结算金额", TEXT_FONT, Element.ALIGN_CENTER,25f));
		table2.addCell(createCell("监理确认量", TEXT_FONT, Element.ALIGN_CENTER,25f));


		Integer totalQuantity = 0;
		for (int i = 0; i < 5; i++) {
			table2.addCell(createCell(String.valueOf(i+1), TEXT_FONT));
			table2.addCell(createCell("108", TEXT_FONT));
			table2.addCell(createCell("交通信号灯安装1-满屏机动灯", TEXT_FONT));
			table2.addCell(createCell("JD400-3—FM31", TEXT_FONT));
			table2.addCell(createCell("1", TEXT_FONT));
			table2.addCell(createCell("套", TEXT_FONT));
			table2.addCell(createCell("2300", TEXT_FONT));
			table2.addCell(createCell("2300", TEXT_FONT));
			table2.addCell(createCell("2300", TEXT_FONT));
			table2.addCell(createCell("", TEXT_FONT));
			totalQuantity ++;
		}


		table2.addCell(createCell(String.valueOf(totalQuantity+1), TEXT_FONT));
		table2.addCell(createCell("", TEXT_FONT));
		table2.addCell(createCell("总价", TEXT_FONT,Element.ALIGN_CENTER,Element.ALIGN_MIDDLE,5,25f));
		table2.addCell(createCell("8600", TEXT_FONT));
		table2.addCell(createCell("8600", TEXT_FONT));
		table2.addCell(createCell("", TEXT_FONT));


		PdfPTable table3 = createTable(new float[] {85,350});
		table3.addCell(createCell("  监理单位(盖章) \n \n  签          字\n \n  日          期", TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
		table3.addCell(createCell("", TEXT_FONT));



		PdfPTable table4 = createTable(new float[] {85,350});
		table4.addCell(createCell("  业主单位(盖章) \n \n  签          字\n \n  日          期", TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
		table4.addCell(createCell("", TEXT_FONT));

		PdfPTable table5 = createTable(new float[] {85,350});
		table5.addCell(createCell("  维护单位(盖章) \n \n  签          字\n \n  日          期", TEXT_FONT,Element.ALIGN_LEFT,Element.ALIGN_MIDDLE,0,80f));
		table5.addCell(createCell("", TEXT_FONT));



		document.add(title);
		document.add(table);
		document.add(table2);

		document.add(table5);
		document.add(table3);
		document.add(table4);


		// 添加图片
		Image image ;

		for (int i = 0; i < 6; i++) {
			image = Image.getInstance("https://img-blog.csdn.net/20180801174617455?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNzg0ODcxMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70");
			image.setAlignment(Image.ALIGN_LEFT);
			image.scalePercent(40); //依照比例缩放
	//		image.setAbsolutePosition(1*i,1*i);
			document.add(image);
		}


	}


/**------------------------创建表格单元格的方法start----------------------------*/


	/**
	 *
	 * @param image  图片
	 * @param   水平对其
	 * @param  合并列
	 * @param
	 * @return
	 */
	public static PdfPCell createCellImage(Image image, float heigth) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		cell.setHorizontalAlignment(align);
		cell.setFixedHeight(heigth);
		cell.setImage(image);
//		cell.setBorder(1);
		// cell.setPhrase(new Phrase(new Chunk(image, 0, 0,false)));
		return cell;

	}


    /**
     * 创建单元格(指定字体)
     * @param value
     * @param font
     * @return
     */
    public static PdfPCell createCell(String value, Font font) {
       return  createCell(value,font,Element.ALIGN_CENTER);
    }
    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
	public static PdfPCell createCell(String value, Font font, int align) {
		return createCell(value,font,align,Element.ALIGN_MIDDLE,1,0);
	}

    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @param height
     * @return
     */
	public static PdfPCell createCell(String value, Font font, int align,float height) {
		return createCell(value,font,align,Element.ALIGN_MIDDLE,1,height);
	}
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并）
     * @param value
     * @param font
     * @param align
     * @param verticalAlign  设置垂直对齐
     * @param colspan
     * @return
     */
    public  static PdfPCell createCell(String value, Font font, int align,int verticalAlign, int colspan,float height) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(verticalAlign);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setCalculatedHeight(35f);
        if (height>0){
        	cell.setFixedHeight(height);
		}
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param boderFlag
     * @return
     */
    public static PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..、边框宽度：0表示无边框、内边距）
     * @param value
     * @param font
     * @param align
     * @param borderWidth
     * @param paddingSize
     * @param flag
     * @return
     */
	public static PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		cell.setBorderWidthLeft(borderWidth[0]);
		cell.setBorderWidthRight(borderWidth[1]);
		cell.setBorderWidthTop(borderWidth[2]);
		cell.setBorderWidthBottom(borderWidth[3]);
		cell.setPaddingTop(paddingSize[0]);
		cell.setPaddingBottom(paddingSize[1]);
		if (flag) {
			cell.setColspan(2);
		}
		return cell;
	}
/**------------------------创建表格单元格的方法end----------------------------*/


/**--------------------------创建表格的方法start------------------- ---------*/
    /**
     * 创建默认列宽，指定列数、水平(居中、右、左)的表格
     * @param colNumber
     * @param align
     * @return
     */
	public static PdfPTable createTable(int colNumber, int align) {
		PdfPTable table = new PdfPTable(colNumber);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(align);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
    /**
     * 创建指定列宽、列数的表格
     * @param widths
     * @return
     */
	public  static PdfPTable createTable(float[] widths) {
		PdfPTable table = new PdfPTable(widths);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
    /**
     * 创建空白的表格
     * @return
     */
	public static PdfPTable createBlankTable() {
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(1);
		table.addCell(createCell("", KEY_FONT));
		table.setSpacingAfter(0.0f);
		table.setSpacingBefore(0.0f);
		return table;
	}
/**--------------------------创建表格的方法end------------------- ---------*/


}
