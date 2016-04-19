package domi.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import domi.core.ApiRequestTemplate;
import domi.core.JedisHelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

@Service("showranking")
@Scope("prototype")
public class showRanking extends ApiRequestTemplate {
    private static final JedisHelper helper = JedisHelper.getInstance();

   
    public showRanking(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("userNo"))) {
            throw new RequestParamException("userNo이 없습니다.");
        }

        if (StringUtils.isEmpty(this.reqData.get("password"))) {
            throw new RequestParamException("password가 없습니다.");
        }
    }

    public void service() throws ServiceException {
        Jedis jedis = null;
    	System.out.println("issue : service");
    	    	
    	String myScore= "123";
        Set<String> enemySet = new HashSet<String>();
        //try 
    	
    	try{                	  
   	 
	        jedis = helper.getConnection();  
	    	enemySet = jedis.zrange("arena:1", 0, 499);

	        this.apiResult.addProperty("resultCode", "200");
            this.apiResult.addProperty("message", "get ranking success");  
            this.apiResult.addProperty("enemySet", enemySet.toString());
    		}
   	 
    	catch (Exception e){
    			helper.returnResource(jedis);
                this.apiResult.addProperty("resultCode", "404");

     	   }

    }
}