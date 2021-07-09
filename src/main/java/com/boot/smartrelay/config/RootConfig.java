package com.boot.smartrelay.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

public class RootConfig {

    @Profile("local")
    @Configuration
    public static class LocalRootConfig{
        public LocalRootConfig(){

        }
    }


}
