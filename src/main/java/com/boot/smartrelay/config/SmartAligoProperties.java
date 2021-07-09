package com.boot.smartrelay.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aligo")
@Getter
@Setter
public class SmartAligoProperties {
    private String id;
    private String sender;
    private String apikey;
}
