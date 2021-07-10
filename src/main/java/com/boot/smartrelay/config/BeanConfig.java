package com.boot.smartrelay.config;

import com.boot.smartrelay.beans.AdminUser;
import com.boot.smartrelay.beans.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class BeanConfig {

    @Bean(name="loginAdminUser")
    @SessionScope
    public AdminUser loginAdminUser(){
        return new AdminUser();
    }

    @Bean
    @SessionScope
    public User loginUser(){
        return new User();
    }

    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5 * 1000);
        factory.setReadTimeout(5 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }
}
