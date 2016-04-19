package domi.service;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.JsonObject;

import domi.core.ApiRequestTemplate;
import domi.core.JedisHelper;
import domi.core.KeyMaker;
import domi.service.dao.TokenKey;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

@Service("userUpdate")
@Scope("prototype")
public class UserUpdate extends ApiRequestTemplate {

	//every some conditinal event 
	//this service provided
	
    @Autowired
    private SqlSession sqlSession;

   
    public UserUpdate(Map<String, String> reqData) {
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
    	//db 작업해주기
    	System.out.println("issue : service");
    	  try {
              Map<String, Object> result = sqlSession.selectOne("users.userInfoByPassword", this.reqData);

              if (result != null) {
      
                  this.apiResult.addProperty("resultCode", "200");
                  this.apiResult.addProperty("message", "Success");
              }
              else {
                  this.apiResult.addProperty("resultCode", "404");
              }
          }
          catch (Exception e) {
        	  //database error
              this.apiResult.addProperty("resultCode", "404");

          }


    }
}