package domi.core;

import com.google.gson.JsonObject;

import domi.service.RequestParamException;
import domi.service.ServiceException;

public interface ApiRequest {

	public void requestParamValidation() throws RequestParamException;

	public void service() throws ServiceException;

	public void executeService();

	public JsonObject getApiResult();
}
