package com.snapadeal.adapters;

import com.snapadeal.constants.SnapADealConstants;
import com.snapadeal.entity.BusinessProfile;
import com.snapadeal.services.SnapADealServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class ViewInterceptorAdapter extends HandlerInterceptorAdapter implements SnapADealConstants {
    @Autowired
    SnapADealServices snapADealServices;

    @Autowired
    HttpSession session;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(null!=session) {
            modelAndView.addObject ( "latitude" , session.getAttribute ( "latitude" ) );
            modelAndView.addObject ( "longitude" , session.getAttribute ( "longitude" ) );
        }

        if(null!=session.getAttribute(USER_TYPE_KEY) && BUSINESSUSER.equalsIgnoreCase((String)session.getAttribute(USER_TYPE_KEY)))
        {
            System.out.println("ViewInterceptorAdapter : postHandle() : Current User Type --> BUSINESSUSER");
            BusinessProfile businessProfile = snapADealServices.getCurrentBusinessUser(false);
            modelAndView.addObject("currentUser",businessProfile);
        }
    }
}
