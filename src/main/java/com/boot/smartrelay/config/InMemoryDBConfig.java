package com.boot.smartrelay.config;

import com.boot.smartrelay.beans.Packet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class InMemoryDBConfig {

    @Bean
    ConcurrentMap<String, List<Packet>> orderConcurrentMap(){
        ConcurrentHashMap<String, List<Packet>> hashMap = new ConcurrentHashMap<>(1000);
        return hashMap;
    }

    @Bean
    ConcurrentMap<String, Long> deviceTimeTable(){
        ConcurrentMap<String, Long> hashMap = new ConcurrentHashMap<>(1000);
        return hashMap;
    }

    @Bean
    ConcurrentMap<String, Boolean> onDeviceMap(){
        ConcurrentMap<String, Boolean> hashMap = new ConcurrentHashMap<>(1000);
        return hashMap;
    }
}
