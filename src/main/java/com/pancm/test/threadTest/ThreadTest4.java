package com.pancm.test.threadTest;

/**
 * Title: ThreadTest4
 * Description:
 * 死锁测试
 * Version:1.0.0
 *
 * @author pancm
 * @date 2018年3月8日
 */
public class ThreadTest4 {
    /**
     * The type Friend.
     */
    static class Friend {
        private final String name;

        /**
         * Instantiates a new Friend.
         *
         * @param name the name
         */
        public Friend(String name) {
            this.name = name;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        /**
         * Bow.
         *
         * @param bower the bower
         */
        public synchronized void bow(Friend bower) {
            System.out.format("%s: %s" + "  早上好!%n", this.name, bower.getName());
            bower.bowBack(this);
        }

        /**
         * Bow back.
         *
         * @param bower the bower
         */
        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s" + " 你也是!%n", this.name, bower.getName());
        }
    }

    /**
     * 两个线程都在等在对方释放资源，但是都不会释放，这就是死锁。
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        final Friend zhangsan = new Friend("张三");
        final Friend lisi = new Friend("李四");
        new Thread(new Runnable() {
            public void run() {
            	zhangsan.bow(lisi);
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
            	lisi.bow(zhangsan);
            }
        }).start();
        
        /**
         * 正常的返回结果应该是:
         *  张三: 李四  早上好!
			李四: 张三 你也是!
			李四: 张三  早上好!
			张三: 李四 你也是!
         * 因为去掉了synchronized同步锁!
         * 
         * 但实际结果是:
         * 张三: 李四  早上好!
		       李四: 张三  早上好!
         * 并且线程不会结束！
         */
        
    }
}
