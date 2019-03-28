package com.pancm.test.design.prototype;

/**
 * The type Prototype test.
 *
 * @author pancm
 * @Title: PrototypeTest
 * @Description: 原型模式   用原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对象。   所谓原型模式，就是java中的克隆技术，以某个对象为原型。复制出新的对象。显然新的对象具备原型对象的特点。  1、实现克隆操作，在 JAVA 继承 Cloneable，重写 clone()。  2、原型模式同样用于隔离类对象的使用者和具体类型（易变类）之间的耦合关系，它同样要求这些"易变类"拥有稳定的接口。
 * @Version:1.0.0
 * @date 2018年8月13日
 */
public class PrototypeTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		Mail mail=new Mail();
		mail.setMsg("生日快乐!");
		Mail mail2=(Mail) mail.clone();
		System.out.println("小明:"+mail.getMsg());
		System.out.println("小红:"+mail2.getMsg());
	}
}

/**
 * The type Mail.
 *
 * @author pancm
 * @Title:
 * @Description: 定义一个原型的邮件信息
 * @Version:1.0.0
 * @date 2018年8月15日
 */
class Mail implements Cloneable {
    /**
     * The Msg.
     */
    protected String msg;

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() {
		return msg;
	}

    /**
     * Sets msg.
     *
     * @param msg the msg
     */
    public void setMsg(String msg) {
		this.msg = msg;
	}

	
	public Object clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}

    /**
     * Send mail.
     */
    void sendMail() {
	}
}

/**
 * The type Birthday mail.
 */
class BirthdayMail extends Mail {

    /**
     * Instantiates a new Birthday mail.
     */
    public BirthdayMail() {
		msg = "生日快乐!";
	}

	@Override
	void sendMail() {
		System.out.println(msg);
	}
}



