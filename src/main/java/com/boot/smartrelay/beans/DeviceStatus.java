package com.boot.smartrelay.beans;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("device-status")
public class DeviceStatus {
    @Id
    String deviceId;
    List<Integer> status;
    List<String> mode;
    long lastSec;
}

