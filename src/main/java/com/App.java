package com;


import java.util.ArrayList;


public class App {
	public ArrayList<String> notes=new ArrayList<String>();
	public void add(String s) {
		notes.add(s);
	}
	
	public int getsize() {
		return notes.size();
	}
	public String Getnotes(int index) {
		return notes.get(index);
	}
	public void add(String s,int location) {
		notes.add(location, s);
	}
	public void removeNotes(int index) {
		notes.remove(index);
	}
	
	public String[] list() {
	String[] a = new String[notes.size()];
	for(int i=0;i<notes.size();i++) {
		a[i]=notes.get(i);
	}
	return a;

}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a=new String[10];
		a[0]="ajkj";
		a[1]="sahsja";

		App nb=new App();

		nb.add("gshaghsagdjas");
		nb.add("dgshadgsjha");
		nb.add("third", 1);
		System.out.println(nb.getsize());
		System.out.println(nb.Getnotes(1));
		nb.removeNotes(1);
		String[] b= nb.list();
	}

}
