package com.pancm.test.arithmetic;

import java.util.*;
import java.text.Collator;

class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "'}";
    }
}

public class SortListByName {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("张三"));
        people.add(new Person("李四"));
        people.add(new Person("阿一"));
        people.add(new Person("王五"));
        people.add(new Person("地三"));
        people.add(new Person("赵六"));

        // 使用Collator进行中文排序
        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                Collator collator = Collator.getInstance(Locale.CHINESE);
                return collator.compare(p1.getName(), p2.getName());
            }
        });

        // 打印排序后的结果
        for (Person person : people) {
            System.out.println(person);
        }
    }
}