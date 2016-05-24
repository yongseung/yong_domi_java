package domi.service;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import domi.core.ApiRequestTemplate;
import domi.core.JedisHelper;
import redis.clients.jedis.Jedis;

@Service("resultSend")
@Scope("prototype")
public class ResultSend extends ApiRequestTemplate {

	@Autowired
	private SqlSession sqlSession;
   
    public ResultSend(Map<String, String> reqData) {
        super(reqData);
    }
    
    @Autowired
	private PlatformTransactionManager transactionManager;

	DefaultTransactionDefinition def = null;
	TransactionStatus status = null;
	

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("MYUUID"))) {
            throw new RequestParamException("myUUID이 없습니다.");
        }

        if (StringUtils.isEmpty(this.reqData.get("ENEMYUUID"))) {
            throw new RequestParamException("enemyUUID이 없습니다.");
        }

        if (StringUtils.isEmpty(this.reqData.get("ACQUIREGOLD"))) {
            throw new RequestParamException("money이 없습니다.");
        }
        
        if (StringUtils.isEmpty(this.reqData.get("ACQUIRESCORE"))) {
            throw new RequestParamException("SCORE이 없습니다.");
        }
        
        
        if (StringUtils.isEmpty(this.reqData.get("LOSTGOLD"))) {
            throw new RequestParamException("lostmoney이 없습니다.");
        }
        
        
        if (StringUtils.isEmpty(this.reqData.get("LOSTSCORE"))) {
            throw new RequestParamException("LOSTSCORE이 없습니다.");
        }
        
    }

    public void service() throws ServiceException {
    	
        Jedis jedis = null;    	
        
    	String userUUID = this.reqData.get("MYUUID");
    	System.out.println(userUUID);
//    	String enemyUUID = this.reqData.get("enemyUUID");
//    
//    	double acquireGold = Double.parseDouble(this.reqData.get("acquireGold"));
//    	double acquireFood = Double.parseDouble(this.reqData.get("acquireFood"));
//    	int acquireScore = Integer.parseInt(this.reqData.get("acquireScore"));
//    	
//    	double lostGold = Double.parseDouble(this.reqData.get("lostGold"));
//    	double lostFood = Double.parseDouble(this.reqData.get("lostFood"));
//    	int lostScore = Integer.parseInt(this.reqData.get("lostScore"));
//    	
    	try {

			def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			status = transactionManager.getTransaction(def);
			
			sqlSession.update("users.userUpdateByMyResult", this.reqData);
			sqlSession.update("users.userUpdateByEnemyResult", this.reqData);


			Map<String, Object> result = sqlSession.selectOne("users.userInfoByUUID", this.reqData);
			if (result != null) {
					this.apiResult.addProperty("score", result.toString());
			}

			this.apiResult.addProperty("resultCode", "200");
			this.apiResult.addProperty("message", "Success");

			transactionManager.commit(status);
			// throw new ServiceException();
		}

		catch (Exception e) {
			// database error
			transactionManager.rollback(status);
			logger.catching(e);
			this.apiResult.addProperty("resultCode", "404");
		}
    	
    	
    	
    	
    	
//    	try{                	  
//   	 
//	        jedis = helper.getConnection();
//	        
//			Transaction t = jedis.multi();    		      
//	        t.hset("inform:"+enemyNo, "status", "accessPossible");
//	   
//	        t.hincrBy("inform:"+enemyNo, "gold", (long)-lostGold);
//	        t.hincrBy("inform:"+enemyNo, "food", (long)-lostFood);
//	        t.hincrBy("inform:"+enemyNo, "medal", (long)-lostMedal);
//
//	        t.hincrBy("inform:"+userNo, "gold", (long)acquireGold);
//	        t.hincrBy("inform:"+userNo, "food", (long)-acquireFood);
//	        t.hincrBy("inform:"+userNo, "medal", (long)acquireMedal);
//	        
//     		t.exec();
//     		
//	     		if(t !=null){
//	     			//success
//	     		     this.apiResult.addProperty("resultCode", "200");
//	                 this.apiResult.addProperty("message", "send success");  
//	     		}
//     
//    		}
//   	 
//     catch (Exception e){
//         helper.returnResource(jedis);
//         this.apiResult.addProperty("resultCode", "404");
//
//     }


    }
}