package com.pancm.test.lockSydTest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Lock test 2.
 *
 * @author pancm
 * @Title: LockTest2
 * @Description: 显示锁的唤醒测试
 * @Version:1.0.0
 * @date 2018年2月28日
 */
public class LockTest2 {
    /**
     * The Lock.
     */
    final Lock lock = new ReentrantLock();

    /**
     * The Notfull.
     */
    final Condition notfull = lock.newCondition();

    /**
     * The Notempty.
     */
    final Condition notempty = lock.newCondition();

    /**
     * The Items.
     */
    final Object[] items = new Object[100];

    /**
     * The Putptr.
     */
    int putptr, /**
     * The Takeptr.
     */
    takeptr, /**
     * The Count.
     */
    count;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
		LockTest2 lt = new LockTest2();
		Object obj = 2;
		lt.put(obj);
		lt.take();
	}

    /**
     * 而现在锁是指定对象lock。所以查找等待唤醒机制方式需要通过lock接口来完成。
     * 而lock接口中并没有直接操作等待唤醒的方法，而是将这些方式又单独封装到了一个对象中。
     * 这个对象就是condition，将object中的三个方法进行单独的封装。 并提供了功能一致的方法
     * await()、signal()、signalall()体现新版本对象的好处。
     *
     * @param x the x
     * @throws InterruptedException the interrupted exception
     */
    public void put(Object x) throws InterruptedException {
		lock.lock();
		try {
			while (count == items.length) {
				notfull.await();
			}
			items[putptr] = x;

			if (++putptr == items.length) {
				putptr = 0;
			}
			++count;
			//唤醒一个等待的线程
			notempty.signal();

		} finally {
			lock.unlock();
		}
	}

	private Object take() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				notempty.await();
			}
			Object x = items[takeptr];

			if (++takeptr == items.length) {
				takeptr = 0;
			}
			--count;
			//唤醒一个等待的线程
			notfull.signal();
			return x;

		} finally {
			lock.unlock();
		}
	}

}
