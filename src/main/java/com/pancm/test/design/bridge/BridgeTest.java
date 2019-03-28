package com.pancm.test.design.bridge;

/**
 * The type Bridge test.
 *
 * @author pancm
 * @Title: BridgeTest
 * @Description:桥接模式 将抽象部分与实现部分分离  ，使它们都可以独立的变化。
 * @Version:1.0.0
 * @date 2018年8月8日
 */
public class BridgeTest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Paper paper=new ExaminationPaper();
		paper.setPen(new RedPen());
		paper.writing();
		
		Paper paper2=new NewsPaper();
		paper2.setPen(new BlackPen());
		paper2.writing();
	}
}

/**
 * The interface Pen.
 *
 * @author pancm
 * @Title: ColourPen
 * @Description: 笔
 * @Version:1.0.0
 * @date 2018年8月22日
 */
interface Pen{
    /**
     * Write.
     */
    void write();
}

/**
 * The type Red pen.
 */
class RedPen implements Pen{
	@Override
	public void write() {
		System.out.println("红色的字");
	}
}

/**
 * The type Black pen.
 */
class BlackPen implements Pen{
	@Override
	public void write() {
		System.out.println("黑色的字");
	}
}


/**
 * The type Paper.
 */
abstract class  Paper{
    /**
     * The Pen.
     */
    protected  Pen pen;

    /**
     * Set pen.
     *
     * @param pen the pen
     */
    void setPen(Pen pen){
		this.pen=pen;
	}

    /**
     * Writing.
     */
    abstract void writing();
}

/**
 * The type Examination paper.
 *
 * @author pancm
 * @Title: ExaminationPaper
 * @Description:考试用的卷子
 * @Version:1.0.0
 * @date 2018年8月22日
 */
class ExaminationPaper extends Paper{
	@Override
	void writing() {
		pen.write();
	}
}

/**
 * The type News paper.
 */
class NewsPaper extends Paper{
	@Override
	void writing() {
		pen.write();
	}
}

