package com.boot.smartrelay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "evercoding")
@Getter
@Setter
public class AdminUserConfigProperties {
    private String userId;
    private String userPw;
}
