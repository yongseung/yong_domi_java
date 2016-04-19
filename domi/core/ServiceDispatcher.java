package domi.core;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Service class dispatcher by uri
 * 
 * @author kris
 */
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

        if (serviceUri.startsWith("/tokens")) {
            String httpMethod = requestMap.get("REQUEST_METHOD");

            if(httpMethod=="POST")
            {
            	System.out.println("access : post");
                beanName = "tokenIssue";

            }
            else if(httpMethod=="DELETE")
            	beanName = "tokenExpier";

            else if(httpMethod=="GET")   
            	beanName = "tokenVerify";
            else 
                beanName = "notFound";
               
        }
        else if (serviceUri.startsWith("/users")) {
            beanName = "users";
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
