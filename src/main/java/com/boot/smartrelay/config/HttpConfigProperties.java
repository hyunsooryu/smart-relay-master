package com.boot.smartrelay.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "smart")
@Getter
@Setter
public class HttpConfigProperties {
    private String httpProtocol;
    private int httpPort;
}
