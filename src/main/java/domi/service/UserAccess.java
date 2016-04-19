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
    	
        Map<String, Object> result = sqlSession.selectOne("users.userInfoByUUID", this.reqData);

        if (result != null) {
            String userNo = String.valueOf(result.get("USERNO"));

            this.apiResult.addProperty("resultCode", "200");
            this.apiResult.addProperty("message", "Success");
            this.apiResult.addProperty("userNo", userNo);
        }
        else {
            this.apiResult.addProperty("resultCode", "404");
            this.apiResult.addProperty("message", "access fail");  

        }

    }
}
