package com.boot.smartrelay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * / -> user/login
 * 리다이렉트 컨트롤러
 * 2021 07 09 류현수
 */
@Controller
@RequestMapping("/")
public class HeadController {
    @GetMapping("/")
    public String redirectToLogin(){
        return "redirect:user/login";
    }
}
