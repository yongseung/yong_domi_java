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

@Service("showMyRanking")
@Scope("prototype")

public class ShowMyRanking extends ApiRequestTemplate {
    private static final JedisHelper helper = JedisHelper.getInstance();

   
    public ShowMyRanking(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("UUID"))) {
            throw new RequestParamException("UUID이 없습니다.");
        }
    }

    public void service() throws ServiceException {
        Jedis jedis = null;
    	    	        
        Long myRank;
        
        String myId = "user:"+(String)this.reqData.get("UUID");
		
        //try 
    	
    	try{                	  
	        jedis = helper.getConnection();  
	        myRank = jedis.zrevrank("ranking", myId);
	    	
	        System.out.println("mRank "+ myRank);

	        this.apiResult.addProperty("resultCode", "200");
            this.apiResult.addProperty("message", "get ranking success");  
            this.apiResult.addProperty("myRank", myRank+1);
    		}
   	 
    	catch (Exception e){
    			helper.returnResource(jedis);
                this.apiResult.addProperty("resultCode", "404");
                this.apiResult.addProperty("message", "get ranking fail");  
     	   }

    }
}