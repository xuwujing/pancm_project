/**
* @Title: package-info
* @Description: 设计模式
设计模式有23种类型。按照主要分类可以分为三大类:

一、创建型模式

这些设计模式提供了一种在创建对象的同时隐藏创建逻辑的方式，而不是使用 new运算符直接实例化对象。这使得程序在判断针对某个给定实例需要创建哪些对象时更加灵活。

单例模式
工厂模式
抽象工厂模式
建造者模式
原型模式
二、结构型模式

这些设计模式关注类和对象的组合。继承的概念被用来组合接口和定义组合对象获得新功能的方式。

适配器模式
桥接模式
过滤器模式
组合模式
装饰器模式
外观模式
享元模式
代理模式
三、行为型模式

这些设计模式特别关注对象之间的通信。

责任链模式
命令模式
解释器模式
迭代器模式
中介者模式
备忘录模式
观察者模式
状态模式
空对象模式
策略模式
模板模式
访问者模式


设计模式的原则
设计模式的六大原则
开闭原则：对扩展开放，对修改关闭。
里氏代换原则：对开闭原则的补充。任何基类可以出现的地方，子类一定可以出现。LSP 是继承复用的基石，只有当派生类可以替换掉基类，且软件单位的功能不受到影响时，基类才能真正被复用，而派生类也能够在基类的基础上增加新的行为。
依赖倒转原则：针对接口编程，依赖于抽象而不依赖于具体。
接口隔离原则：尽量使用多个隔离的接口，为了降低类之间的耦合度。
迪米特法则：一个实体应当尽量少地与其他实体之间发生相互作用，使得系统功能模块相对独立。
合成复用原则：尽量使用合成/聚合的方式，而不是使用继承。



* @Version:1.0.0  
* @author pancm
* @date 2018年7月7日
*/
package com.pancm.test.design;