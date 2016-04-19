package domi.service;

import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import domi.core.ApiRequestTemplate;
import domi.core.JedisHelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

@Service("resultSend")
@Scope("prototype")
public class ResultSend extends ApiRequestTemplate {
    private static final JedisHelper helper = JedisHelper.getInstance();

   
    public ResultSend(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("userNo"))) {
            throw new RequestParamException("userNo이 없습니다.");
        }

        if (StringUtils.isEmpty(this.reqData.get("enemyNo"))) {
            throw new RequestParamException("enemyNo이 없습니다.");
        }

        if (StringUtils.isEmpty(this.reqData.get("acquireGold"))) {
            throw new RequestParamException("gold이 없습니다.");
        }
        
        if (StringUtils.isEmpty(this.reqData.get("acquireFood"))) {
            throw new RequestParamException("food이 없습니다.");
        }
        
        if (StringUtils.isEmpty(this.reqData.get("acquireMedal"))) {
            throw new RequestParamException("medal이 없습니다.");
        }
        
        
        if (StringUtils.isEmpty(this.reqData.get("lostGold"))) {
            throw new RequestParamException("gold이 없습니다.");
        }
        
        if (StringUtils.isEmpty(this.reqData.get("lostFood"))) {
            throw new RequestParamException("food이 없습니다.");
        }
        
        if (StringUtils.isEmpty(this.reqData.get("lostMedal"))) {
            throw new RequestParamException("medal이 없습니다.");
        }
        
    }

    public void service() throws ServiceException {
        Jedis jedis = null;    	
    	String enemyNo = this.reqData.get("enemyNo");
    	String userNo = this.reqData.get("userNo");
    
    	int acquireGold = Integer.parseInt(this.reqData.get("acquireGold"));
    	int acquireFood = Integer.parseInt(this.reqData.get("acquireFood"));
    	int acquireMedal = Integer.parseInt(this.reqData.get("acquireMedal"));
    	int lostGold = Integer.parseInt(this.reqData.get("lostGold"));
    	int lostFood = Integer.parseInt(this.reqData.get("lostFood"));
    	int lostMedal = Integer.parseInt(this.reqData.get("lostMedal"));
    	
    	
        //try 
    	
    	try{                	  
   	 
	        jedis = helper.getConnection();
	        
			Transaction t = jedis.multi();    		      
	        t.hset("inform:"+enemyNo, "status", "accessPossible");
	   
	        t.hincrBy("inform:"+enemyNo, "gold", (long)-lostGold);
	        t.hincrBy("inform:"+enemyNo, "food", (long)-lostFood);
	        t.hincrBy("inform:"+enemyNo, "medal", (long)-lostMedal);

	        t.hincrBy("inform:"+userNo, "gold", (long)acquireGold);
	        t.hincrBy("inform:"+userNo, "food", (long)-acquireFood);
	        t.hincrBy("inform:"+userNo, "medal", (long)acquireMedal);
	        
     		t.exec();
     		
	     		if(t !=null){
	     			//success
	     		     this.apiResult.addProperty("resultCode", "200");
	                 this.apiResult.addProperty("message", "send success");  
	     		}
     
    		}
   	 
     catch (Exception e){
         helper.returnResource(jedis);
         this.apiResult.addProperty("resultCode", "404");

     }


    }
}