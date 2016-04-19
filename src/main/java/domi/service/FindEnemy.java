package domi.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import domi.core.ApiRequestTemplate;
import domi.core.JedisHelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

@Service("findEnemy")
@Scope("prototype")
public class FindEnemy extends ApiRequestTemplate {
    private static final JedisHelper helper = JedisHelper.getInstance();

   
    public FindEnemy(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("userNo"))) {
            throw new RequestParamException("userNo이 없습니다.");
        }
        if (StringUtils.isEmpty(this.reqData.get("era"))) {
            throw new RequestParamException("userNo이 없습니다.");
        }
        if (StringUtils.isEmpty(this.reqData.get("score"))) {
            throw new RequestParamException("userNo이 없습니다.");
        }

    }

    public void service() throws ServiceException {
        Jedis jedis = null;
        
    	String score = this.reqData.get("score");
    	String era	 = this.reqData.get("era");
    	
    	int category = Integer.parseInt(score)/100; 
    	String myArena ="arena:"+ String.valueOf(category);
    	
        Set<String> enemySet = new HashSet<String>();
    	Map<String,String> enemyInfo = new HashMap<String,String>();
       
    	try{                	  

	        jedis = helper.getConnection();
	    	enemySet = jedis.zrangeByScore(myArena, score, score+100, 0, 20);
	    	
	    	for(String it : enemySet){
	    		enemyInfo =jedis.hgetAll("inform:"+it);
	    	
	    		if(enemyInfo.get("era").toCharArray()[0] >= era.toCharArray()[0]){
	    			
	    			if(enemyInfo.get("status") == "accessPosible"){
	    				Map <String,String> temp = new HashMap<String,String>();         		
	    				temp.put("status", "enemyObserve");

	    				jedis.watch("inform:"+it);
	    				
	    				Transaction t = jedis.multi();    		
	  
	             		t.hmset(("inform:"+it), temp);
	             		Response<String> result = t.hget(("inform:"+it),"status");       		
	             		t.exec();
	             		
	            		if(result.get() == "enemyObserve"){
	            		      Gson gson = new Gson();
	            		      String enemyMap =enemyInfo.get("mapData").toString();	            		      
	                          JsonObject data = gson.fromJson(enemyMap, JsonObject.class);
	                         	                                      
	              		     this.apiResult.addProperty("resultCode", "200");
                             this.apiResult.addProperty("message", "find success");  
                             this.apiResult.addProperty("enemyMap", data.toString());               		
	            			
                             return;
	            			//send
	            		}				
	    				//종료
	    			}
	    			
	    			
	    			
	    			
	    		}
	    	
	    	}
   	}
   	 
     catch (Exception e){
         helper.returnResource(jedis);
         this.apiResult.addProperty("resultCode", "404");
         this.apiResult.addProperty("message", "Find enemy fail");  


     }


    }
}