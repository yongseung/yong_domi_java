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

@Service("resultsend")
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
        
    }

    public void service() throws ServiceException {
        Jedis jedis = null;
    	System.out.println("issue : service");
    	

    	String enemyNo = this.reqData.get("enemyNo");
    	String userNo = this.reqData.get("userNo");
    	
    	int acquireGold = Integer.parseInt(this.reqData.get("acquireGold"));
    	int acquireFood = Integer.parseInt(this.reqData.get("acquireFood"));
    	int acquireMedal = Integer.parseInt(this.reqData.get("acquireMedal"));


    	Map<String,String> userInfo = new HashMap<String,String>();
        //try 
    	
    	try{                	  
   	 
	        jedis = helper.getConnection();
	        
			Transaction t = jedis.multi();    		      
	        t.hset("inform:"+enemyNo, "status", "accessPossible");
	   
	        t.hincrBy("inform:"+enemyNo, "gold", (long)-acquireGold);
	        t.hincrBy("inform:"+enemyNo, "gold", (long)-acquireFood);
	        t.hincrBy("inform:"+enemyNo, "gold", (long)-acquireMedal);

	        t.hincrBy("inform:"+userNo, "gold", (long)-acquireGold);
	        t.hincrBy("inform:"+userNo, "gold", (long)-acquireFood);
	        t.hincrBy("inform:"+userNo, "gold", (long)-acquireMedal);
	        
     		t.exec();
     		
     		if(t !=null){
     			//success
     		     this.apiResult.addProperty("resultCode", "200");
                 this.apiResult.addProperty("message", "send success");  
     		}

	        //db 에 데이터 저장하기, 리스트 데이터 만들기
	        //레디스에 상태 바꿔주기~       
    		}
   	 
     catch (Exception e){
         helper.returnResource(jedis);
         this.apiResult.addProperty("resultCode", "404");

     }


    }
}