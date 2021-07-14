package com.boot.smartrelay.config;


import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

//@Configuration
@RequiredArgsConstructor
public class TomcatConfig {

    final HttpConfigProperties httpConfigProperties;

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcatServletWebServerFactory;
    }

    private Connector createStandardConnector(){
        Connector connector = new Connector(httpConfigProperties.getHttpProtocol());
        connector.setPort(httpConfigProperties.getHttpPort());
        return connector;
    }
}
