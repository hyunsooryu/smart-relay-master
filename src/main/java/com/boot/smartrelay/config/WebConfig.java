package com.boot.smartrelay.config;

import com.boot.smartrelay.beans.AdminUser;
import com.boot.smartrelay.beans.User;
import com.boot.smartrelay.interceptor.AdminInterceptor;
import com.boot.smartrelay.interceptor.PageInterceptor;
import com.boot.smartrelay.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    final AdminUserConfigProperties adminUserConfigProperties;

    final AdminUser adminUser;

    final User loginUser;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //AdminInterceptor 적용
        AdminInterceptor adminInterceptor = new AdminInterceptor(adminUser, adminUserConfigProperties.getUserId(), adminUserConfigProperties.getUserPw());
        InterceptorRegistration adminReg = registry.addInterceptor(adminInterceptor);
        adminReg.addPathPatterns("/admin/main", "/admin/main_pro", "/admin/main_add_pro", "/admin/main_del_pro");


        //PageInterceptor 적용
        //PageInterceptor pageInterceptor = new PageInterceptor(varRoot.get("http"), varRoot.get("https"));
        //InterceptorRegistration pageReg = registry.addInterceptor(pageInterceptor);
        //pageReg.addPathPatterns("/**");

        //UserInterceptor 적용
        UserInterceptor userInterceptor = new UserInterceptor(loginUser);
        InterceptorRegistration userReg = registry.addInterceptor(userInterceptor);
        userReg.addPathPatterns("/user/logout", "/user/lsec/**", "/user/ssec/**", "/user/detail/**", "/user/device/**", "/device/user/**", "/device/status/**", "/modify/**");

    }


}
