package domi.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import domi.core.ApiRequestTemplate;
import domi.core.JedisHelper;
import domi.core.KeyMaker;
import domi.service.dao.TokenKey;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service("tokenIssue")
@Scope("prototype")
public class TryLogin extends ApiRequestTemplate {
    private static final JedisHelper helper = JedisHelper.getInstance();

    @Autowired
    private SqlSession sqlSession;

    public TryLogin(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("userNo"))) {
            throw new RequestParamException("userNo이 없습니다.");
        }
        
        if (StringUtils.isEmpty(this.reqData.get("era"))) {
            throw new RequestParamException("era가 없습니다.");
        }
        
        if (StringUtils.isEmpty(this.reqData.get("score"))) {
            throw new RequestParamException("score이 없습니다.");
        }
    }

    public void service() throws ServiceException {
        Jedis jedis = null;
    	System.out.println("issue : service");

         //   Map<String, Object> result = sqlSession.selectOne("users.userInfoByPassword", this.reqData);
        	//userNumber 준다고 가정하자 그냥
        	
                String userNo = String.valueOf(reqData.get("userNo"));
             
                jedis = helper.getConnection();
                Map<String,String> m_myInform = new HashMap<>();
                
                m_myInform.put("era", reqData.get("era"));
                m_myInform.put("score", reqData.get("score"));
                
                try{
                	 if(!jedis.exists("inform:"+userNo)){
                     	//아이디 없으면 db에서 만들어서 user number 가져온다 지금은 임시로
                         m_myInform.put("status", "accessPossible");
                		 jedis.hmset("inform:"+userNo, m_myInform);
                     }
                }
                
                catch (Exception e) {
                    helper.returnResource(jedis);
                }
                
                try{                	  
	                	if(jedis.hget(("inform:"+userNo),"status") == "accessPossible"){
	                		jedis.watch("inform:"+userNo);
	                		Map <String,String> temp = new HashMap<String,String>();
	             		
	                		temp.put("status", "loginOn");
	                		
	                		Transaction t = jedis.multi();    		
	                		
	                		t.hmset(("inform:"+userNo), temp);
	                		Response<String> result = t.hget(("inform:"+userNo),"status");       		
	                		t.exec();
	                		
		                		if(result.get() == "loginOn"){
		                    	//내 동료 정보 더 가져오기
		                		     this.apiResult.addProperty("resultCode", "200");
		                             this.apiResult.addProperty("message", "login success");               		
		                		}
	                		}
	                	
	                	else if(jedis.get("inform:"+userNo) == "loginOn"){
	                		//another device access
	                        this.apiResult.addProperty("message", "another device on");	               

	                	}
	                	else if(jedis.get("inform:"+userNo) == "onDefense"){
	                		
	                        this.apiResult.addProperty("message", "onDefense");	               

	                	}
	                	else if(jedis.get("inform:"+userNo) == "enemyObserve"){
	               		
	                        this.apiResult.addProperty("message", "enemy is observing");	               

	                	}
	                	
	                    this.apiResult.addProperty("resultCode", "400");
	                }                   
                
                catch (Exception e){
                          helper.returnResource(jedis);
                          this.apiResult.addProperty("resultCode", "404");

                }
       }                            
}
