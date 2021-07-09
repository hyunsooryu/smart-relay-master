package com.boot.smartrelay;


import com.boot.smartrelay.beans.DeviceStatus;
import com.boot.smartrelay.repository.DeviceStatusMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {
    @Autowired
    DeviceStatusMemoryRepository deviceStatusMemoryRepository;



}
