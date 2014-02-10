package com.server;

import java.rmi.*;
import java.util.List;

public interface ActionInterface extends Remote{
	List<Integer> getRange() throws RemoteException;//Serwer przekazanie zakresu obliczeń do klienta
	void setListResults(List<Long> x) throws RemoteException; //Klient przekazuje liste do serwera
}