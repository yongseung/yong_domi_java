package domi.service;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import domi.core.ApiRequestTemplate;

@Service("userAccess")
@Scope("prototype")
public class UserAccess extends ApiRequestTemplate {
	@Autowired
	private SqlSession sqlSession;

	public UserAccess(Map<String, String> reqData) {
		super(reqData);
	}

	public void requestParamValidation() throws RequestParamException {
		if (StringUtils.isEmpty(this.reqData.get("UUID"))) {
			throw new RequestParamException("uuid이 없습니다.");
		}
	}

	public void service() throws ServiceException {
		// db 작업해주기
		try {

			Map<String, Object> result = sqlSession.selectOne("users.userInfoByUUID", this.reqData);
			if (result != null) {

				System.out.println(result.toString());

				this.apiResult.addProperty("resultCode", "200");
				this.apiResult.addProperty("message", "Success");
				this.apiResult.addProperty("userName", String.valueOf(result.get("USERNAME")));
				this.apiResult.addProperty("money", String.valueOf(result.get("MONEY")));
				this.apiResult.addProperty("food", String.valueOf(result.get("FOOD")));
				this.apiResult.addProperty("score", String.valueOf(result.get("SCORE")));

			} else {
				this.apiResult.addProperty("resultCode", "404");
				this.apiResult.addProperty("message", "access fail");
			}

		} catch (Exception e) {
			// database error
			logger.catching(e);
			this.apiResult.addProperty("resultCode", "404");
		}

	}
}
