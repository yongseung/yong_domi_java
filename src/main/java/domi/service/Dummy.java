package domi.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import domi.core.JedisHelper;

import redis.clients.jedis.Jedis; 

public class Dummy{
    private static final JedisHelper helper = JedisHelper.getInstance();

   
    public Dummy() {        
    	System.out.println("call dummy");
        Jedis jedis = null;
    	    	        
        Set<String> enemySet = new HashSet<String>();
        //try 
    	
        String user = "user";
    	try{                	  
	        jedis = helper.getConnection();  
	        
	        for(int i =0; i<100000; i++){
	        	System.out.println(jedis.zadd("ranking", i, user+String.valueOf(i)));    	
	        }
	    	
	        enemySet = jedis.zrange("ranking", 0, 499);
	        System.out.println(enemySet.toString());
    		}
   	 
    	catch (Exception e){
    			helper.returnResource(jedis);      
     	   }
    }
}