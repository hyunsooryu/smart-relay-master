package com.boot.smartrelay.beans;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUser {
    private String id;
    private String password;
    private boolean isLogin;

    public AdminUser(){
        this.isLogin = false;
    }
}
