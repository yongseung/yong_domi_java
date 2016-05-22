package domi;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;


import domi.core.JedisHelper;
import redis.clients.jedis.Jedis;

public class ApiServerMain {
    public static void main(String[] args) {
        AbstractApplicationContext springContext = null;
        try {
            springContext = new AnnotationConfigApplicationContext(ApiServerConfig.class);
            springContext.registerShutdownHook();

            ApiServer server = springContext.getBean(ApiServer.class);
            System.out.println("server on");
            final JedisHelper helper = JedisHelper.getInstance();
            Jedis jedis = null;
            

            jedis = helper.getConnection();
            jedis.set("hi", "yo man");
            System.out.print(jedis.get("hi"));  
            
            server.start();
                    

            

        }
        catch(Exception e){
        	e.printStackTrace();
        	System.out.println("aa");
        	e.getMessage();
        	
        }
        finally {
            springContext.close();
        }
    }
}
