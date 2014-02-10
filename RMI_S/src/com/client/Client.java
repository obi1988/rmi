package com.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.data.CalculateTh;
import com.data.Sdbm;
import com.server.ActionInterface;

public class Client{
	
	private static List<Long> m = Collections.synchronizedList(new ArrayList<Long>());
	private static List<Long> m_sort = Collections.synchronizedList(new ArrayList<Long>());
	private static List<Long> m_res = Collections.synchronizedList(new ArrayList<Long>());
	
    static public void main(String args[]){
       ActionInterface rmiServer;
       Registry registry;
       String serverAddress= args[0];
       String serverPort= args[1];
       try{
    	   System.out.println("sending to "+serverAddress+":"+serverPort);
    	   registry=LocateRegistry.getRegistry(serverAddress,Integer.valueOf(serverPort));
    	   try{
    		   rmiServer= (ActionInterface)(registry.lookup("rmiServer"));
    		   List<Integer> list_range = rmiServer.getRange();//pobranie zakresu
	
	    	   calculate(list_range);//obliczanie
	    	   System.out.println("Client: Rozmiar listy do wyslania " + m.size());
	    	   rmiServer.setListResults(m_res);//wyslanie na serwer wyniku
    	   }catch(NotBoundException ex){
    		   ex.printStackTrace();
    	   }
       }catch(RemoteException e){
           e.printStackTrace();
       }
    }
    
    /**
     * Obliczenia klienta
     * @param list_range
     */
    private static void calculate(List<Integer> list_range) {
		try {
			Runnable[] runners = new Runnable[5];
			Thread[] threads = new Thread[5];
			for (int i = 0; i < 5; i++) {
				runners[i] = new CalculateTh(list_range.get(i), list_range.get(i+1), m);
				threads[i] = new Thread(runners[i]);
				threads[i].start();
			}
			for (int i = 0; i < 5; i++) {
				threads[i].join();
			}
			System.out.println("Map size: " + m.size());
			m_sort = Sdbm.sortByValue(m);
			int count = 0;
			
			for(Long entry : m_sort){
				if(count > m_sort.size() - 11 ){
					System.out.println(entry);
					m_res.add(entry);
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}