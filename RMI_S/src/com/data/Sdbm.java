package com.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sdbm {

	public static Long hash(byte[] b){
		Long n = new Long(0);
        int len = b.length;

        for (int i=0;i<len;i++) {
        	n = b[i] + (n << 6) + (n << 16) - n;
        }
        return n;
    }
    
    public static byte[] intToBytes(int my_int) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeInt(my_int);
        out.close();
        byte[] int_bytes = bos.toByteArray();
        bos.close();
        return int_bytes;
    }
    
    
    public static List<Long> getHashFromInt(int a, int b, ArrayList<Long> mm) throws Exception{
    	
    	Long res_int = new Long(0);
    	
    	for(int i =a; i<=b; i++){
    		byte [] byte_array = toBytes(i); //zamiana int na tab byte
    		res_int = hash(byte_array);
    		mm.add(res_int);
    	}
    	return mm;
    }
    
    static byte[] toBytes(int i)
    {
      byte[] result = new byte[4];

      result[0] = (byte) (i >> 24);
      result[1] = (byte) (i >> 16);
      result[2] = (byte) (i >> 8);
      result[3] = (byte) (i /*>> 0*/);

      return result;
    }
    
    public static List<Long> sortByValue(List<Long> map) {
       Collections.sort(map);
       return map;
   } 
    
    public static List<Long> getFirstValue(List<Long> m ){
    	List<Long> mapa = new ArrayList<Long>();
    	int count = 0;
    	
    	for(Long entry : m){
			if(count > m.size() - 11 ){
				mapa.add(entry);
			}
			count++;
    	}
    	return mapa;
    }
}
