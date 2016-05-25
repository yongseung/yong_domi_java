package domi.core;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServiceDispatcher {
	private static ApplicationContext springContext;

	@Autowired
	public void init(ApplicationContext springContext) {
		ServiceDispatcher.springContext = springContext;
	}

	static Logger logger = LogManager.getLogger(ServiceDispatcher.class.getName());

	public static ApiRequest dispatch(Map<String, String> requestMap) {
		String serviceUri = requestMap.get("REQUEST_URI");
		String beanName = null;

		if (serviceUri == null) {
			beanName = "notFound";
		}

		if (serviceUri.startsWith("/logins")) {
			String httpMethod = requestMap.get("REQUEST_METHOD");
			if (httpMethod == "POST") {
				beanName = "trylogin";
			}
		}
		
		else if (serviceUri.startsWith("/enemies")) {
			beanName = "findEnemy";
		}

		else if (serviceUri.startsWith("/users")) {
			String httpMethod = requestMap.get("REQUEST_METHOD");
			if (httpMethod == "POST") {
				System.out.println("user : post");
				beanName = "userAccess";

			}
		}
		else if (serviceUri.startsWith("/showranking")) {
			String httpMethod = requestMap.get("REQUEST_METHOD");
			if (httpMethod == "POST") {
				System.out.println("show ranking : post");
				beanName = "showRanking";

			}
		}
		
		else if (serviceUri.startsWith("/showmyranking")) {
			String httpMethod = requestMap.get("REQUEST_METHOD");
			if (httpMethod == "POST") {
				System.out.println("show ranking : post");
				beanName = "showMyRanking";

			}
		}

		else if (serviceUri.startsWith("/resultsend")) {
			String httpMethod = requestMap.get("REQUEST_METHOD");
			if (httpMethod == "POST") {
				System.out.println("results send : post");
				beanName = "resultSend";

			}
		}
		else if (serviceUri.startsWith("/updateuser")) {
			String httpMethod = requestMap.get("REQUEST_METHOD");
			if (httpMethod == "POST") {
				System.out.println("update : post");
				beanName = "userUpdate";
			}
		}
		else {
			beanName = "notFound";
		}

		ApiRequest service = null;

		try {
			logger.trace(requestMap.toString());
			service = (ApiRequest) springContext.getBean(beanName, requestMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.catching(e);
			service = (ApiRequest) springContext.getBean("notFound", requestMap);
		}

		return service;
	}
}
