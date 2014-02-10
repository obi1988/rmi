package com.data;

import java.util.ArrayList;
import java.util.List;

public class CalculateTh implements Runnable{

	private List<Long> map;
	private int a;
	private int b;
	
	public CalculateTh(int a, int b, List<Long> map){
		this.map = map;
		this.a = a;
		this.b = b;
	}
	
	@Override
	public void run() {
			addMaps();			
	}
	private  void addMaps(){
		try{
			map.addAll(Sdbm.getFirstValue(Sdbm.getHashFromInt(a, b,new ArrayList<Long>())));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
