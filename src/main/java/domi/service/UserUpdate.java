package domi.service;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
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

	// every some conditinal event
	// this service provided

	@Autowired
	private SqlSession sqlSession;

	public UserUpdate(Map<String, String> reqData) {
		super(reqData);
	}

	public void requestParamValidation() throws RequestParamException {
		if (StringUtils.isEmpty(this.reqData.get("UUID"))) {
			throw new RequestParamException("UUID이 없습니다.");
		}
	}

	public void service() throws ServiceException {

		// db 작업해주기
		// sqlSession.update("users.userUpdateByUUID", this.reqData);

			System.out.print(this.reqData.toString());
		try {		
			int k =sqlSession.update("users.userUpdateByUUID", this.reqData);
			
			if(k == 1){
				Map<String, Object> result = sqlSession.selectOne("users.userInfoByUUID", this.reqData);
				if (result != null) {
					this.apiResult.addProperty("result", result.toString());
				
				}
				this.apiResult.addProperty("resultCode", "200");
				this.apiResult.addProperty("message", "Success");		
			}
		}

		catch (Exception e) {
			// database error
			logger.catching(e);
			this.apiResult.addProperty("resultCode", "404");
		}

	}
}