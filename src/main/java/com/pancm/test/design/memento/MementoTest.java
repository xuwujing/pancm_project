package com.pancm.test.design.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: MementoTest
 * @Description: 备忘录模式 备忘录模式（Memento
 *               Pattern）保存一个对象的某个状态，以便在适当的时候恢复对象。备忘录模式属于行为型模式。
 *               在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态。
 * @Version:1.0.0
 * @author pancm
 * @date 2018年10月29日
 */
public class MementoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/*
		 * 基本使用
		 */
		Originator originator = new Originator();
		CareTaker careTaker = new CareTaker();
		originator.setState("State #1");
		originator.setState("State #2");
		careTaker.add(originator.saveStateToMemento());
		originator.setState("State #3");
		careTaker.add(originator.saveStateToMemento());
		originator.setState("State #4");

		System.out.println("Current State: " + originator.getState());
		originator.getStateFromMemento(careTaker.get(0));
		System.out.println("First saved State: " + originator.getState());
		originator.getStateFromMemento(careTaker.get(1));
		System.out.println("Second saved State: " + originator.getState());

		
		
		
		/*
		 * 优点： 1、给用户提供了一种可以恢复状态的机制，可以使用户能够比较方便地回到某个历史的状态。 2、实现了信息的封装，使得用户不需要关心状态的保存细节。
		 * 缺点：消耗资源。如果类的成员变量过多，势必会占用比较大的资源，而且每一次保存都会消耗一定的内存。
		 */

	}

}

/*
 * 创建一个备忘录
 */
class Memento {
	private String state;

	public Memento(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}
}

/*
 * 设置一个起因
 */
class Originator {
	private String state;

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public Memento saveStateToMemento() {
		return new Memento(state);
	}

	public void getStateFromMemento(Memento Memento) {
		state = Memento.getState();
	}
}

/*
 * 设置一个看门者
 */
class CareTaker {
	private List<Memento> mementoList = new ArrayList<Memento>();

	public void add(Memento state) {
		mementoList.add(state);
	}

	public Memento get(int index) {
		return mementoList.get(index);
	}
}


