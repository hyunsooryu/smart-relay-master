package com.boot.smartrelay.interceptor;

import com.boot.smartrelay.beans.AdminUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ${pageContext.request.contextPath} = root
 *
 */
public class AdminInterceptor implements HandlerInterceptor {

    private AdminUser adminUser;

    public AdminInterceptor(AdminUser adminUser, String userId, String userPw){
       this.adminUser = adminUser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //로그인 하지 않았다면
        if(!adminUser.isLogin()){
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/admin/not_login");
            return false;
        }
        return true;
    }
}
