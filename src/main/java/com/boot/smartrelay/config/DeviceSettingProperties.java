package com.boot.smartrelay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "device")
@Getter
@Setter
public class DeviceSettingProperties {
    private String version;
}
