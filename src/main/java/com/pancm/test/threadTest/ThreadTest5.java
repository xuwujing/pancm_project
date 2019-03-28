package com.pancm.test.threadTest;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Title: ThreadTest5
 * Description:
 * ThreadTest4 的改进版
 * 通过Lock来实现
 * Version:1.0.0
 *
 * @author pancm
 * @date 2018年3月8日
 */
public class ThreadTest5 {
    /**
     * The type Friend.
     */
    static class Friend {
        private final String name;
        private final Lock lock = new ReentrantLock();

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
         * Impending bow boolean.
         *
         * @param bower the bower
         * @return the boolean
         */
        public boolean impendingBow(Friend bower) {
            Boolean myLock = false;
            Boolean yourLock = false;
            try {
                myLock = lock.tryLock();
                yourLock = bower.lock.tryLock();
            } finally {
                if (!(myLock && yourLock)) {
                    if (myLock) {
                        lock.unlock();
                    }
                    if (yourLock) {
                        bower.lock.unlock();
                    }
                }
            }
            return myLock && yourLock;
        }

        /**
         * Bow.
         *
         * @param bower the bower
         */
        public void bow(Friend bower) {
            if (impendingBow(bower)) {
                try {
                    System.out.format("%s: %s has" + " bowed to me!%n", this.name, bower.getName());
                    bower.bowBack(this);
                } finally {
                    lock.unlock();
                    bower.lock.unlock();
                }
            } else {
                System.out.format(
                        "%s: %s started" + " to bow to me, but saw that" + " I was already bowing to" + " him.%n",
                        this.name, bower.getName());
            }
        }

        /**
         * Bow back.
         *
         * @param bower the bower
         */
        public void bowBack(Friend bower) {
            System.out.format("%s: %s has" + " bowed back to me!%n", this.name, bower.getName());
        }
    }

    /**
     * The type Bow loop.
     */
    static class BowLoop implements Runnable {
        private Friend bower;
        private Friend bowee;

        /**
         * Instantiates a new Bow loop.
         *
         * @param bower the bower
         * @param bowee the bowee
         */
        public BowLoop(Friend bower, Friend bowee) {
            this.bower = bower;
            this.bowee = bowee;
        }

        public void run() {
            Random random = new Random();
            for (;;) {
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                }
                bowee.bow(bower);
            }
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");
        new Thread(new BowLoop(alphonse, gaston)).start();
        new Thread(new BowLoop(gaston, alphonse)).start();
    }
}
