package com.boot.smartrelay.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PageInterceptor implements HandlerInterceptor {

    private String httpRoot;
    private String httpsRoot;

    public PageInterceptor(String httpRoot, String httpsRoot){
        this.httpRoot = httpRoot;
        this.httpsRoot = httpsRoot;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        request.setAttribute("httpRoot", httpRoot);
        request.setAttribute("httpsRoot", httpsRoot);
    }
}
