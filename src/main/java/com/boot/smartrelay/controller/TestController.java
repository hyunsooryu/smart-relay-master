package com.boot.smartrelay.controller;

import com.boot.smartrelay.schedule.SmartAligoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {


    final SmartAligoApiService smartAligoApiService;

    @GetMapping("/sms")
    public String smsTest(){
        boolean result = smartAligoApiService.apiTest();
        return String.valueOf(result);
    }

}
