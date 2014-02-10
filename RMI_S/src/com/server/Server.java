package com.server;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.data.CalculateTh;
import com.data.Sdbm;

public class Server extends java.rmi.server.UnicastRemoteObject implements ActionInterface{

	private static List<Long> m = Collections.synchronizedList(new ArrayList<Long>());
	private static final long serialVersionUID = 1L;
	int port;
	String adress;
	Registry registry;
	
	
	public static void main(String args[])
	{
		try {
			new Server();
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	/**
	 * Przesłanie wyniku od klienta.
	 */
	public void setListResults(List<Long> x) throws RemoteException{
		
			m.addAll(x);
			System.out.println("Server: Otrzymalem elementow: " + x.size());
			System.out.println("Server: Wszystkich elementow mapy: " + m.size());
			sortMaps();
	}

	public Server() throws RemoteException{
		try {
			adress = (InetAddress.getLocalHost()).toString();
		}catch (Exception e) {
			throw new RemoteException("can't get inet address.");
		}
		
		port = 3232; // port servera
		System.out.println("this address=" + adress + ",port=" + port);
		
		try {
			registry = LocateRegistry.createRegistry(port);
			registry.rebind("rmiServer", this);
			
		}catch (RemoteException e) {
			throw e;
		}
	}

	/**
	 * Obliczenia serwera zakres: 500k - 1mln
	 */
	 private static void calculate() {
			try {
				int zakres = 500000;
				Runnable[] runners = new Runnable[5];
				Thread[] threads = new Thread[5];
				for (int i = 0; i < 5; i++) {
					runners[i] = new CalculateTh(zakres, zakres + 100000, m);
					threads[i] = new Thread(runners[i]);
					threads[i].start();
					zakres += 100000;
				}
				for (int i = 0; i < 5; i++) {
					threads[i].join();
				}
				System.out.println("Map size: " + m.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 
	 /**
	  * Sortowanie listy oraz wyświetlenie wyniku
	  */
	 private static void sortMaps(){
		 m = Sdbm.sortByValue(m);
			int count = 0;
			
			for(Long entry : m){
				if(count > m.size() - 11 ){
					System.out.println(entry);
				}
				count++;
			}
	 }

	 /**
	  * Przesłanie klientowi listy z zakresami do liczenia
	  * zakres: 0 - 500k
	  */
	@Override
	public List<Integer> getRange() throws RemoteException {

		List<Integer> list = new ArrayList<Integer>();
		
		for(int i=0; i < 6; i++){
			list.add(i*100000);
		}
		calculate(); //liczy server
		return list;
	}
}