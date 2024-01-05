package com;


import java.util.ArrayList;


/**
 * @Author pancm
 * @Description 
 * @Date  2024/1/4
 * @Param 
 * @return 
 **/
public class App {
    //声明一个ArrayList类型的变量notes
    public ArrayList<String> notes = new ArrayList<String>();

    //声明一个add方法，用于添加字符串
    public void add(String s) {
        notes.add(s);
    }

    //声明一个getsize方法，用于获取notes的大小
    public int getsize() {
        return notes.size();
    }

    //声明一个getNotes方法，用于获取notes中指定位置的字符串
    public String getNotes(int index) {
        return notes.get(index);
    }

    //声明一个add方法，用于添加字符串，并指定添加的位置
    public void add(String s, int location) {
        notes.add(location, s);
    }

    //声明一个removeNotes方法，用于移除notes中指定位置的字符串
    public void removeNotes(int index) {
        notes.remove(index);
    }

    //声明一个list方法，用于获取notes中的所有字符串
    public String[] list() {
        String[] a = new String[notes.size()];
        for (int i = 0; i < notes.size(); i++) {
            a[i] = notes.get(i);
        }
        return a;


    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //声明一个长度为10的字符串数组a
        String[] a = new String[10];
        //给a数组赋值
        a[0] = "ajkj";
        a[1] = "sahsja";

        //声明一个App类型的变量nb
        App nb = new App();
        //调用nb的add方法，添加字符串
        nb.add("gshaghsagdjas");
        nb.add("dgshadgsjha");
        nb.add("third", 1);
        //调用nb的getsize方法，获取notes的大小
        System.out.println(nb.getsize());
        //调用nb的getNotes方法，获取notes中指定位置的字符串
        System.out.println(nb.getNotes(1));
        //调用nb的removeNotes方法，移除notes中指定位置的字符串
        nb.removeNotes(1);
        //调用nb的list方法，获取notes中的所有字符串
        String[] b = nb.list();

        //写一个调用高德地图的接口，调用逆地址编码，将结果转换成json格式，使用文件保存，写入到项目中

    }

}