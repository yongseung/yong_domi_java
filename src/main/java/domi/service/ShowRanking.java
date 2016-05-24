package domi.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import domi.core.ApiRequestTemplate;
import domi.core.JedisHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple; 

@Service("showRanking")
@Scope("prototype")

public class ShowRanking extends ApiRequestTemplate {
    private static final JedisHelper helper = JedisHelper.getInstance();

   
    
    public ShowRanking(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("UUID"))) {
            throw new RequestParamException("UUID이 없습니다.");
        }
    }

    public void service() throws ServiceException {
        Jedis jedis = null;
    	    	        
        Set<Tuple> enemySet;        
		Map<String, Integer> temp = new LinkedHashMap<String, Integer>();
		
        //try 
    	
    	try{                	  
	        jedis = helper.getConnection();  
	    	enemySet = jedis.zrevrangeWithScores("ranking", 0, 300);
	    	
	   		for(Tuple tuple: enemySet){
	   			temp.put(tuple.getElement(), (int)tuple.getScore());
	    		 System.out.println(tuple.getElement() + "-" + tuple.getScore());
	    		 }

	        this.apiResult.addProperty("resultCode", "200");
            this.apiResult.addProperty("message", "get ranking success");  
            this.apiResult.addProperty("enemySet", temp.toString());
    		}
   	 
    	catch (Exception e){
    			helper.returnResource(jedis);
                this.apiResult.addProperty("resultCode", "404");
                this.apiResult.addProperty("message", "get ranking fail");  
     	   }

    }
}
