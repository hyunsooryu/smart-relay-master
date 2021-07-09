package com.boot.smartrelay.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequestMapping("/")
public class AppController {

    @GetMapping("/detail")
    public String detail_sector(){
        return "user/detail_sector";
    }

    @GetMapping("/device")
    public String device_sector(){
        return "user/device_sector";
    }

    @GetMapping("/testboard")
    public String testboard(){
        return "testboard";
    }
}
