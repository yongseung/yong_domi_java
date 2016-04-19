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

    protected Logger logger = LogManager.getLogger(this.getClass());

    public static ApiRequest dispatch(Map<String, String> requestMap) {
        String serviceUri = requestMap.get("REQUEST_URI");
        String beanName = null;

        if (serviceUri == null) {
            beanName = "notFound";
        }

        if (serviceUri.startsWith("/logins")) {
            String httpMethod = requestMap.get("REQUEST_METHOD");
            if(httpMethod=="POST")
            {
                beanName = "trylogin";

            }
        }
        else if (serviceUri.startsWith("/enemies")) {
            beanName = "findEnemy";
        }
        
        else if (serviceUri.startsWith("/user")) {
            String httpMethod = requestMap.get("REQUEST_METHOD");
            if(httpMethod=="POST")
            {
            	System.out.println("login : post");
                beanName = "userAccess";

            }
        }
        
        else if (serviceUri.startsWith("/results")) {
            String httpMethod = requestMap.get("REQUEST_METHOD");
            if(httpMethod=="POST")
            {
            	System.out.println("login : post");
                beanName = "resultSend";

            }
        }
        else if (serviceUri.startsWith("/up")) {
            String httpMethod = requestMap.get("REQUEST_METHOD");
            if(httpMethod=="POST")
            {
                beanName = "userUpdate";

            }
        }
        
        
        else {
            beanName = "notFound";
        }

        ApiRequest service = null;
        
        
        try {
            service = (ApiRequest) springContext.getBean(beanName, requestMap);
        }
        catch (Exception e) {
            e.printStackTrace();
            service = (ApiRequest) springContext.getBean("notFound", requestMap);
        }

        return service;
    }
}
