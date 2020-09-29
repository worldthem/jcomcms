package ecommercejava.cms.icommyjava.config;


import ecommercejava.cms.icommyjava.helper.ViewHelper;
import ecommercejava.cms.icommyjava.helper.ViewHelpersRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Component
public class ViewInterceptor implements HandlerInterceptor, ApplicationContextAware {


    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Autowired
    ViewHelper viewHelper;

    @Override
    public boolean preHandle(HttpServletRequest aRequest, HttpServletResponse aResponse, Object aHandler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest aRequest, HttpServletResponse aResponse, Object aHandler, ModelAndView aModelAndView) throws Exception {
        if(aModelAndView != null) {
            Principal user = aRequest.getUserPrincipal();
           // aModelAndView.addObject("__user", user);

            aModelAndView.addObject("view", getApplicationContext().getBean("viewHelper") );
            aModelAndView.addObject("viewRequest", new ViewHelpersRequest(aRequest));
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest aRequest, HttpServletResponse aResponse, Object aHandler, Exception aEx) throws Exception { }

}