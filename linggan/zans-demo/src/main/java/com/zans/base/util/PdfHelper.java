package com.zans.base.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.zans.base.config.GlobalConstants;

public class PdfHelper {

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
            BaseFont bfChinese = BaseFont.createFont(GlobalConstants.UPLOAD_FOLDER+ "/template/pdf/simsun.ttc,1" , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            TITLE_FONT = new Font(bfChinese, 18, Font.BOLD);
            HEAD_FONT = new Font(bfChinese, 14, Font.NORMAL);
            KEY_FONT = new Font(bfChinese, 10, Font.NORMAL);
            TAIL_FONT = new Font(bfChinese, 11, Font.NORMAL);
            TEXT_FONT = new Font(bfChinese, 12, Font.NORMAL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/**------------------------创建表格单元格的方法start----------------------------*/


	/**
	 *
	 * @param image  图片
	 * @param align  水平对其
	 * @param colspan 合并列
	 * @param rowspan
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
